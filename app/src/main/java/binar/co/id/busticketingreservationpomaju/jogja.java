package binar.co.id.busticketingreservationpomaju;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class Jogja extends android.support.v7.widget.AppCompatTextView {

    public Jogja(Context context){
        super(context);
    }
    public  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int blank_width = 4;
    private int dark_gray = 0xFF7597B3;
    private int silver = 0xFFC0C0C0;
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = this.getPaint();
        paint.reset();

        paint.setColor(dark_gray);

        canvas.drawLine(0, blank_width, this.getWidth(), blank_width, paint);
        canvas.drawLine(4*this.getWidth()/5, 0, this.getWidth(), blank_width, paint);

        super.onDraw(canvas);
    }

}