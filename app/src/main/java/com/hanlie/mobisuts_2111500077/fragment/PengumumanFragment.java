package com.hanlie.mobisuts_2111500077.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanlie.mobisuts_2111500077.ClassGlobal;
import com.hanlie.mobisuts_2111500077.MainActivity;
import com.hanlie.mobisuts_2111500077.R;
import com.hanlie.mobisuts_2111500077.adapter.PengumumanAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PengumumanFragment extends Fragment {

    SwipeRefreshLayout swipe;
    RecyclerView rvPengumuman;
    RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    PengumumanAdapter adapter;

    private void connectDB() {
        String url = ((ClassGlobal) getActivity().getApplication()).getUrl() + "pengumuman.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean hasil = jObj.getBoolean("hasil");
                        if (!hasil) {
                            Toast.makeText(getActivity(), jObj.getString("pesan"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray jsonArray = jObj.getJSONArray("resCekPengumuman");
                        arrayList.clear();
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject json = jsonArray.getJSONObject(a);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("idpengumuman", json.optString("ID_PENGUMUMAN", ""));
                            map.put("JUDUL", json.optString("CPENGUMUMAN", ""));
                            map.put("COVER", json.optString("CCOVER", ""));
                            map.put("DMULAI", json.optString("DMULAI", ""));
                            map.put("DAKHIR", json.optString("DAKHIR", ""));
                            map.put("DESKRIPSI", json.optString("CDESKRIPSI", ""));
                            arrayList.add(map);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        swipe.setRefreshing(false);
                    }
                },
                error -> {
                    swipe.setRefreshing(false);
                    Toast.makeText(getActivity(), "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("aksi", "cekpengumuman");
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);

        rvPengumuman = view.findViewById(R.id.rvPengumuman);
        rvPengumuman.setLayoutManager(new LinearLayoutManager(getContext()));

        swipe = view.findViewById(R.id.swipe);
        requestQueue = Volley.newRequestQueue(getActivity());

        adapter = new PengumumanAdapter((MainActivity) getActivity(), arrayList);
        rvPengumuman.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> {
            swipe.setRefreshing(true);
            connectDB();
        });

        connectDB();

        return view;
    }
}
