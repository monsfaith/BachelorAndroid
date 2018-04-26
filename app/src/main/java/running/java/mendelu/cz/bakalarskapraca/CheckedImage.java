package running.java.mendelu.cz.bakalarskapraca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Monika on 05.04.2018.
 */

public class CheckedImage extends android.support.v7.widget.AppCompatImageView{
        private boolean checked = true;

        public CheckedImage(Context context) {
            super(context);
        }

        public CheckedImage(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CheckedImage(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
            invalidate();
        }

        public boolean isChecked() {
            return checked;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(checked) {
                Bitmap check = BitmapFactory.decodeResource(
                        getResources(), R.drawable.checked1);
                int width = check.getWidth();
                int height = check.getHeight();
                int margin = 25;
                int x = canvas.getWidth() - width - margin;
                int y = canvas.getHeight() - height - margin;
                canvas.drawBitmap(check, x, y, new Paint());
            }
        }
    }

