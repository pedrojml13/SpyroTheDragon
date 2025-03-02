package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.*;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;

    // Contador y handler estáticos para gestionar la lógica globalmente
    private static int pulsacionesGemas = 0;
    private static final Handler handler = new Handler(Looper.getMainLooper());


    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {

        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);


        holder.itemView.setOnClickListener(v -> {
            if ("gemas".equalsIgnoreCase(collectible.getName())) {
                pulsacionesGemas++;

                // Reiniciamos el contador si no se pulsa en 1 segundo
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> pulsacionesGemas = 0, 1000);

                if (pulsacionesGemas == 4) {
                    pulsacionesGemas = 0;
                    handler.removeCallbacksAndMessages(null); // Limpiar el handler

                    // Iniciamos la actividad de video en pantalla completa
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FullScreenVideoActivity.class);
                    intent.putExtra("videoPath", "android.resource://" + context.getPackageName() + "/" + R.raw.easter_egg);
                    context.startActivity(intent);
                }
            } else {
                // Si se pulsa otro elemento, reseteamos el contador
                pulsacionesGemas = 0;
                handler.removeCallbacksAndMessages(null);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
