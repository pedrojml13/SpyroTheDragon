package dam.pmdm.spyrothedragon.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class FlameAnimationView extends View {

    private Paint flamePaint;
    private Paint particlePaint;
    private Path flamePath;
    private float flameHeight = 0;
    private float flameWidth = 100;
    private Random random;
    private ValueAnimator animator;

    public FlameAnimationView(Context context) {
        super(context);
        init();
    }

    public FlameAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlameAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        flamePaint = new Paint();
        flamePaint.setStyle(Paint.Style.FILL);

        particlePaint = new Paint();
        particlePaint.setColor(Color.YELLOW); // Color para las partículas

        random = new Random();

        flamePath = new Path();

        // Definimos la animación para la llama
        animator = ValueAnimator.ofFloat(0, 200);
        animator.setDuration(2000);  // Duración de la animación

        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(animation -> {
            flameHeight = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        flamePaint.setColor(Color.rgb(255, 69, 0));  // Color de la llama

        flamePath.reset();

        // Hacemos que la forma de la llama varíe
        float fluctuation = (random.nextFloat() - 0.5f) * 100;
        flameWidth = 100 + fluctuation;

        flamePath.moveTo(getWidth() / 2, 0);
        flamePath.quadTo(getWidth() / 2 - flameWidth, flameHeight / 2, getWidth() / 2, flameHeight);
        flamePath.quadTo(getWidth() / 2 + flameWidth, flameHeight / 2, getWidth() / 2, 0);

        // Dibujamos la llama
        canvas.drawPath(flamePath, flamePaint);

        // Dibujamos las partículas alrededor de la llama
        for (int i = 0; i < 20; i++) {
            float x = getWidth() / 2 + (random.nextFloat() - 0.5f) * (flameWidth * 0.5f);
            float y = random.nextFloat() * flameHeight;
            canvas.drawCircle(x, y, 3, particlePaint);
        }
    }

    // Método para iniciar la animación de la llama
    public void startFlameAnimation() {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
        invalidate();  // Redibujar la vista
    }

    // Método para detener la animación
    public void stopFlameAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }
}
