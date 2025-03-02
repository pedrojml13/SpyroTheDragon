package dam.pmdm.spyrothedragon.adapters;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import dam.pmdm.spyrothedragon.R;

public class FullScreenVideoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);

        VideoView videoView = findViewById(R.id.videoViewFullScreen);

        // Obtener la ruta del video desde el intent
        String videoPath = getIntent().getStringExtra("videoPath");
        if (videoPath != null) {
            videoView.setVideoURI(Uri.parse(videoPath));
            videoView.start();
        }

        // Cerrar la actividad cuando el video termine
        videoView.setOnCompletionListener(MediaPlayer::release);
        videoView.setOnCompletionListener(mp -> finish());

        // También cerrar al hacer clic en la pantalla
        videoView.setOnClickListener(v -> finish());
    }
}
