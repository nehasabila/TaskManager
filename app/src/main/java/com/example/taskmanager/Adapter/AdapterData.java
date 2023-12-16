package com.example.taskmanager.Adapter;

import android.content.Context;
import android. content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.API.APIRequestData;
import com.example.taskmanager.API.RetroServer;
import com.example.taskmanager.Activity.MainActivity;
import com.example.taskmanager.Activity.UbahActivity;
import com.example.taskmanager. Model. DataModel;
import com.example.taskmanager.Model.ResponseModel;
import com.example.taskmanager.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listTugas;
    private int idPa;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override

    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.vId.setText(String.valueOf(dm.getId()));
        holder.vJudul.setText(dm.getJudul());
        holder.vDeskripsi.setText(dm.getDeskripsi());
        holder.vTanggal.setText(dm.getTanggal());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView vId, vJudul, vDeskripsi, vTanggal;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            vId = itemView.findViewById(R.id.v_id);
            vJudul = itemView.findViewById(R.id.v_judul);
            vDeskripsi = itemView.findViewById(R.id.v_deskripsi);
            vTanggal = itemView.findViewById(R.id.v_tanggal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override

                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    idPa = Integer.parseInt(vId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }
        private void deleteData() {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idPa);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData() {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idPa);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listTugas = response.body().getData();

                    int varIdPa = listTugas.get(0).getId();
                    String varJudul = listTugas.get(0).getJudul();
                    String varDeskripsi = listTugas.get(0).getDeskripsi();
                    String varTanggal = listTugas.get(0).getTanggal();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("jId", varIdPa);
                    kirim.putExtra("jJudul", varJudul);
                    kirim.putExtra("jDeskripsi", varDeskripsi);
                    kirim.putExtra("jTanggal", varTanggal);
                    ctx.startActivity(kirim);
                }


                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}



