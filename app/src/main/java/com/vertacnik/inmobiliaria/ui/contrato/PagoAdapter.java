package com.vertacnik.inmobiliaria.ui.contrato;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vertacnik.inmobiliaria.R;
import com.vertacnik.inmobiliaria.modelo.Pago;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {
    private List<Pago> pagos;
    private Context context;
    private LayoutInflater layoutInflater;

    public PagoAdapter(List<Pago> pagos, Context context, LayoutInflater layoutInflater) {
        this.pagos = pagos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_pago, parent, false);
        return new PagoViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago pagoActual = pagos.get(position);

        holder.codigo.setText("Código pago: " + pagoActual.getIdPago());
        //La API no trae "número de pago": usamos la posición en la lista (igual que el prototipo 1, 2, 3...)
        holder.numero.setText("Número de pago: " + (position + 1));
        holder.contrato.setText("Código de contrato: " + pagoActual.getIdContrato());
        holder.importe.setText("Importe: $" + pagoActual.getMonto());

        // Cambiar el formato de la fecha a uno normal
        LocalDate fecha = LocalDate.parse(pagoActual.getFechaPago());
        String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        holder.fecha.setText("Fecha de pago: " + fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return pagos.size();
    }

    //Clase interna - Representacion de la Card
    public class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView codigo;
        TextView numero;
        TextView contrato;
        TextView importe;
        TextView fecha;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.tvPagoCodigo);
            numero = itemView.findViewById(R.id.tvPagoNumero);
            contrato = itemView.findViewById(R.id.tvPagoContrato);
            importe = itemView.findViewById(R.id.tvPagoImporte);
            fecha = itemView.findViewById(R.id.tvPagoFecha);
        }
    }
}
