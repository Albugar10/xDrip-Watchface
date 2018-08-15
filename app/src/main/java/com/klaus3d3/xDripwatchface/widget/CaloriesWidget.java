package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.sport.SlptTodayCaloriesView;

import java.util.Collections;
import java.util.List;

import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.data.Calories;
import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.resource.ResourceManager;



public class CaloriesWidget extends AbstractWidget {
    private TextPaint textPaint;
    private Calories  calories;

    private float textTop;
    private float textLeft;

    private boolean showUnits;
    private boolean caloriesBool;
    private boolean caloriesAlignLeftBool;
    private LoadSettings settings;

    public CaloriesWidget(LoadSettings settings) {
        this.settings = settings;
    }

    @Override
    public void init(Service service) {
        this.textLeft = service.getResources().getDimension(R.dimen.calories_text_left);
        this.textTop = service.getResources().getDimension(R.dimen.calories_text_top);

        // Aling left true or false (false= align center)
        this.caloriesAlignLeftBool = service.getResources().getBoolean(R.bool.calories_left_align);

        this.textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(service.getResources().getColor(R.color.calories_colour));
        this.textPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.textPaint.setTextSize(service.getResources().getDimension(R.dimen.calories_font_size));
        this.textPaint.setTextAlign( (this.caloriesAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        // Enable/Disable
        this.caloriesBool = service.getResources().getBoolean(R.bool.calories);

        // Show units boolean
        this.showUnits = service.getResources().getBoolean(R.bool.calories_units);
    }

    @Override
    public List<DataType> getDataTypes() {
        return Collections.singletonList(DataType.CALORIES);
    }

    @Override
    public void onDataUpdate(DataType type, Object value) {
        this.calories = (Calories) value;
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY, int minutes, int hours) {
        if(this.caloriesBool) {
            String units = (showUnits) ? " kcal" : "";
            //String text = String.format("%s", calories.getCalories());
            canvas.drawText(calories.getCalories() + units, textLeft, textTop, textPaint);
        }
    }

    // SLPT mode, screen off
    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
       SlptLinearLayout caloriesLayout = new SlptLinearLayout();
        caloriesLayout.add(new SlptTodayCaloriesView());
        // Show or Not Units
        if(service.getResources().getBoolean(R.bool.calories_units)) {
            SlptPictureView caloriesUnit = new SlptPictureView();
            caloriesUnit.setStringPicture(" kcal");
            caloriesLayout.add(caloriesUnit);
        }
        caloriesLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.calories_font_size),
                service.getResources().getColor(R.color.calories_colour_slpt),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE)
        );
        // Position based on screen on
        caloriesLayout.alignX = 2;
        caloriesLayout.alignY = 0;
        int tmp_left = (int) service.getResources().getDimension(R.dimen.calories_text_left);
        if(!service.getResources().getBoolean(R.bool.calories_left_align)) {
            // If text is centered, set rectangle
            caloriesLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.calories_font_size))
            );
            tmp_left = -320;
        }
        caloriesLayout.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.calories_text_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.calories_font_size))
        );
        if(!service.getResources().getBoolean(R.bool.calories)){caloriesLayout.show=false;}

        return Collections.<SlptViewComponent>singletonList(caloriesLayout);
    }
}
