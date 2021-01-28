package ise308.ozkan.ezgi.ozkan_ezgi_freelancejobapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_add_job.*
import kotlinx.android.synthetic.main.activity_update_job.view.*
import kotlinx.android.synthetic.main.listitem.view.*

class FreelancerAdapter(fCtx: Context, val jobs: ArrayList<Freelancer>):
    RecyclerView.Adapter<FreelancerAdapter.ViewHolder>()
{

    val fCtx = fCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitle = itemView.jobTitle
        val jobDescription = itemView.jobDescription
        val daysLeft = itemView.daysLeft
        val jobPrice = itemView.jobPrice
        val cardView = itemView.cardView
        val btnUpdate = itemView.btnUpdate
        val btnDelete = itemView.btnDelete


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreelancerAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.listitem,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: FreelancerAdapter.ViewHolder, position: Int) {


        val job: Freelancer = jobs[position]
        holder.jobTitle.text = job.jobTitle
        holder.jobDescription.text = job.jobDescription
        holder.daysLeft.text = job.daysLeft.toString() + " day"
        holder.jobPrice.text = "$" + job.jobPrice.toString()


        //If the user selects the checkbox box as check, the cardview color will appear as blue, otherwise red.

        if (job.jobCheck == true){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E7F2F8"))
        }else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F8E8E8"))
        }


        //A message box is created for the user to confirm the deletion.

        holder.btnDelete.setOnClickListener {

            val jobTitle = job.jobTitle
            var alertDialog = AlertDialog.Builder(fCtx)
                .setTitle("Warning")
                .setMessage("Are you sure to delete? ")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteJob(job.jobID)){

                        jobs.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,jobs.size)
                        Toast.makeText(fCtx,"Job $jobTitle Deleted",Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(fCtx,"Error Deleting",Toast.LENGTH_SHORT).show()
                    }

                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .show()


        }


        //For edit the job posting

        holder.btnUpdate.setOnClickListener{
            val inflater = LayoutInflater.from(fCtx)
            val view = inflater.inflate(R.layout.activity_update_job,null)

            val updateJobTitle: TextView = view.findViewById(R.id.updateJobTitle)
            val updateJobDescription : TextView = view.findViewById(R.id.updateJobDescription)
            val updateJobPrice : TextView = view.findViewById(R.id.updateJobPrice)
            val updateDaysLeft: TextView = view.findViewById(R.id.updateDaysLeft)
            val updateJobCheck: CheckBox = view.findViewById(R.id.updateJobCheck)
            updateJobTitle.text = job.jobTitle
            updateJobDescription.text = job.jobDescription
            updateJobPrice.text = job.jobPrice.toString()
            updateDaysLeft.text = job.daysLeft.toString()
            updateJobCheck.isChecked = job.jobCheck!!


            val builder = AlertDialog.Builder(fCtx)
                    .setTitle("Update Freelance Job Info")
                    .setView(view)
                    .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                        val isUpdate : Boolean = MainActivity.dbHandler.updateJob(
                             job.jobID.toString(),
                                view.updateJobTitle.text.toString(),
                                view.updateJobDescription.text.toString(),
                                view.updateJobPrice.text.toString(),
                               view.updateDaysLeft.text.toString(),
                                view.updateJobCheck.text.toString())

               //The data is updated when the user is in the position that the user
               // wants to update in the recycler view.

                        if (isUpdate == true) {
                            jobs[position].jobTitle = view.updateJobTitle.text.toString()
                            jobs[position].jobDescription = view.updateJobDescription.text.toString()
                            jobs[position].jobPrice = view.updateJobPrice.text.toString().toDouble()
                            jobs[position].daysLeft = view.updateDaysLeft.text.toString().toInt()
                            jobs[position].jobCheck = view.updateJobCheck.isChecked



                            notifyDataSetChanged()
                            Toast.makeText(fCtx,"Update Successfuly",Toast.LENGTH_SHORT).show()

                        }else{

                            Toast.makeText(fCtx,"Error Updating",Toast.LENGTH_SHORT).show()
                        }


                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                    })
                    val alert= builder.create()
            alert.show()




        }




    }


}