package dam.pmdm.spyrothedragon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.views.FlameAnimationView;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;
    FlameAnimationView flameView;

    public CharactersAdapter(List<Character> charactersList, FlameAnimationView flameView) {
        this.list = charactersList;
        this.flameView = flameView;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Detectamos si hay pulsación larga en el botón Spyro
        if ("spyro".equalsIgnoreCase(character.getName())) {
            holder.imageImageView.setOnLongClickListener(v -> {
                int[] location = new int[2];
                holder.imageImageView.getLocationOnScreen(location);

                // Calculamos la posición X y Y de la llama
                float x = location[0] - holder.imageImageView.getWidth() / 2 - 140;
                float y = location[1] + holder.imageImageView.getHeight() / 2 - 200;

                // Establecemos la posición de la llama
                flameView.setX(x);
                flameView.setY(y);

                // Mostramos la llama
                flameView.setVisibility(View.VISIBLE);

                // Reiniciamos la animación
                flameView.startFlameAnimation(); // Reinicia la animación de la llama

                // Esperamos el tiempo que dura la animación antes de ocultar la llama
                flameView.postDelayed(() -> flameView.setVisibility(View.GONE), 2000);

                return true;
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
