package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;


import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.sport.SlptTodayFloorNumView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.data.TodayFloor;
import com.klaus3d3.xDripwatchface.resource.ResourceManager;


public class FloorWidget extends AbstractWidget {
    private TextPaint textPaint;
    private TodayFloor todayFloor;
    private String text;
    private float textTop;
    private float textLeft;
    private Boolean floorsBool;
    private Boolean floorAlignLeftBool;
    private String[] digitalNums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private LoadSettings settings;

    public FloorWidget(LoadSettings settings) {
        this.settings = settings;
    }

    @Override
    public void init(Service service){
        this.textLeft = service.getResources().getDimension(R.dimen.floors_text_left);
        this.textTop = service.getResources().getDimension(R.dimen.floors_text_top);

        // Aling left true or false (false= align center)
        this.floorAlignLeftBool = service.getResources().getBoolean(R.bool.floor_left_align);

        this.textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(service.getResources().getColor(R.color.floors_colour));
        this.textPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.textPaint.setTextSize(service.getResources().getDimension(R.dimen.floors_font_size));
        this.textPaint.setTextAlign( (this.floorAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.floorsBool = service.getResources().getBoolean(R.bool.floor);
    }

    @Override
    public List<DataType> getDataTypes() {
        return Collections.singletonList(DataType.FLOOR);
    }

    @Override
    public void onDataUpdate (DataType type, Object value) {this.todayFloor = (TodayFloor) value;}

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY, int minutes, int hours) {
        // Draw floors
        if(this.floorsBool) {
            this.text = String.format("%s", todayFloor.getFloor());
            canvas.drawText(text, textLeft, textTop, textPaint);
        }
    }

    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        // Draw floors
        SlptLinearLayout floors = new SlptLinearLayout();
        floors.add(new SlptTodayFloorNumView());
        floors.setStringPictureArrayForAll(this.digitalNums);
        floors.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.floors_font_size),
                service.getResources().getColor(R.color.floors_colour_slpt),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE)
        );
        // Position based on screen on
        floors.alignX = 2;
        floors.alignY = 0;
        int tmp_left = (int) service.getResources().getDimension(R.dimen.floors_text_left);
        if(!service.getResources().getBoolean(R.bool.floor_left_align)) {
            // If text is centered, set rectangle
            floors.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.floors_font_size))
            );
            tmp_left = -320;
        }
        floors.setStart(
                tmp_left,
                (int) (service.getResources().getDimension(R.dimen.floors_text_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.floors_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.floor)){floors.show=false;}

        return Arrays.asList(new SlptViewComponent[]{floors});
    }
}
