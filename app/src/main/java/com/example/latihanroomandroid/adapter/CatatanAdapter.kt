package com.example.latihanroomandroid.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanroomandroid.MainActivity
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.dataclass.Catatan
import com.example.latihanroomandroid.roomdatabase.CatatanDatabase
import kotlinx.android.synthetic.main.custom_layout_dialog_edit_data.view.*
import kotlinx.android.synthetic.main.custom_layout_dialog_hapus_data.view.*
import kotlinx.android.synthetic.main.item_adapter_catatan.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class CatatanAdapter(
    private val listCatatan : List<Catatan>,
    private val OnClick : (Catatan) -> Unit
) : RecyclerView.Adapter<CatatanAdapter.ViewHolder>() {
    private var dbCatatan : CatatanDatabase? = null
    //Define ViewHolder Class
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_catatan, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_judul.text = "Judul : \n${listCatatan[position].judul}"
        holder.itemView.tv_waktu.text = "Waktu : \n${listCatatan[position].waktu}"

        //Delete data
        holder.itemView.button_delete.setOnClickListener {
            dbCatatan = CatatanDatabase.getInstance(it.context)

            //create custom dialog for delete process
            val customDialogDelete = LayoutInflater.from(it.context)
                .inflate(R.layout.custom_layout_dialog_hapus_data, null, false)
            val hapusDataDialog = AlertDialog.Builder(it.context)
                .setView(customDialogDelete)
                .create()

            //cancel delete action
            customDialogDelete.hapus_dialog_button_cancel.setOnClickListener {
                hapusDataDialog.dismiss()
            }

            //delete action button
            customDialogDelete.hapus_dialog_button_hapus.setOnClickListener {
                GlobalScope.async {

                    //command for room database
                    val command = dbCatatan?.catatanDao()?.deleteDataCatatan(listCatatan[position])

                    //check if delete process worked or not
                    (customDialogDelete.context as MainActivity).runOnUiThread{
                        if(command != 0){
                            Toast.makeText(customDialogDelete.context, "Catatan ${listCatatan[position].judul} berhasil dihapus", Toast.LENGTH_SHORT).show()
                            //recreate activity after delete process
                            (customDialogDelete.context as MainActivity).recreate()
                        }else{
                            Toast.makeText(customDialogDelete.context, "Catatan ${listCatatan[position].judul} gagal dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            hapusDataDialog.show()
        }

        //edit data
        holder.itemView.button_edit.setOnClickListener {
            dbCatatan = CatatanDatabase.getInstance(it.context)

            //create dialog for edit action
            val customDialogEdit = LayoutInflater.from(it.context)
                .inflate(R.layout.custom_layout_dialog_edit_data, null, false)
            val editDataDialog = AlertDialog.Builder(it.context)
                .setView(customDialogEdit)
                .create()

            //initialize edit text with previous "judul" and "catatan"
            customDialogEdit.edit_input_judul.setText(listCatatan[position].judul)
            customDialogEdit.edit_input_catatan.setText(listCatatan[position].catatan)

            //edit action button
            customDialogEdit.edit_button_update_data.setOnClickListener {
                //get new data
                val newJudul = customDialogEdit.edit_input_judul.text.toString()
                val newCatatan = customDialogEdit.edit_input_catatan.text.toString()

                //re-initialize data of listCatatan that in current position
                listCatatan[position].judul = newJudul
                listCatatan[position].catatan = newCatatan

                GlobalScope.async {
                    //command for room database
                    val command = dbCatatan?.catatanDao()?.updateDataCatatan(listCatatan[position])

                    //check if edit process worked or not
                    (customDialogEdit.context as MainActivity).runOnUiThread{
                        if(command != 0){
                            Toast.makeText(it.context, "Catatan berhasil diupdate", Toast.LENGTH_SHORT).show()
                            //recreate activity
                            (customDialogEdit.context as MainActivity).recreate()
                        }else{
                            Toast.makeText(it.context, "Catatan gagal diupdate", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            editDataDialog.show()
        }

        holder.itemView.card.setOnClickListener {
            OnClick(listCatatan[position])
        }
    }

    override fun getItemCount(): Int {
        return listCatatan.size
    }
}