package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import java.util.List;

public class PagerHomeAdapter extends RecyclerView.Adapter<PagerHomeAdapter.PagerViewHolder> {
    private final List<Integer> imageList;

    public PagerHomeAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }


    @NonNull
    @Override
    public PagerHomeAdapter.PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerHomeAdapter.PagerViewHolder holder, int position) {

        holder.imageView.setImageResource(imageList.get(position));

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class PagerViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
