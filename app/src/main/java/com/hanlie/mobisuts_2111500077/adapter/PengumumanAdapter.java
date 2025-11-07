package com.hanlie.mobisuts_2111500077.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.hanlie.mobisuts_2111500077.ClassGlobal;
import com.hanlie.mobisuts_2111500077.MainActivity;
import com.hanlie.mobisuts_2111500077.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<HashMap<String, String>> list_data;

    public PengumumanAdapter(MainActivity mainAct, ArrayList<HashMap<String, String>> list_data) {
        this.context = mainAct;
        this.list_data = list_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pengumuman, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> data = list_data.get(position);

        // --- Ambil field sesuai struktur JSON dari cekpengumuman.php ---
        String cover_pengumuman = data.get("COVER");
        String judul_pengumuman = data.get("JUDUL");
        String deskripsi_pengumuman = data.get("DESKRIPSI");
        String mulai_pengumuman = data.get("DMULAI");
        String akhir_pengumuman = data.get("DAKHIR");

        // Sinkronisasi IP address backend lokal
        if (cover_pengumuman != null && cover_pengumuman.startsWith("http://127.0.0.1")) {
            cover_pengumuman = cover_pengumuman.replace("http://127.0.0.1", ClassGlobal.global_ipaddress);
        }

        // Transisi halus Glide
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        Glide.with(context)
                .load(cover_pengumuman)
                .transition(withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_blokir)
                .into(holder.imgPengumuman);

        // Set teks tampilan
        holder.tvJdlPengumuman.setText(judul_pengumuman);
        holder.tanggal.setText(String.format("%s - %s", mulai_pengumuman, akhir_pengumuman));
        holder.deskripsi.setText(deskripsi_pengumuman);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPengumuman;
        TextView tvJdlPengumuman, tanggal, deskripsi;
        CardView cvPengumuman;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPengumuman = itemView.findViewById(R.id.imgPengumuman);
            tvJdlPengumuman = itemView.findViewById(R.id.tvJdlPengumuman);
            tanggal = itemView.findViewById(R.id.tanggal);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            cvPengumuman = itemView.findViewById(R.id.cvPengumuman);
        }
    }
}
