package ise308.ozkan.ezgi.ozkan_ezgi_freelancejobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_job.*

class AddJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)
        btnSave.setOnClickListener {

            if (updateJobTitle.text.isEmpty()){

                Toast.makeText(this,"Enter job title ", Toast.LENGTH_SHORT).show()
                updateJobTitle.requestFocus()
            }
            else
            {

                //created an object named job from the Freelancer class.
                val job = Freelancer()
                job.jobTitle = updateJobTitle.text.toString()
                job.jobDescription = updateJobDescription.text.toString()
                job.daysLeft = updateDaysLeft.text.toString().toInt()
                job.jobCheck = updateJobCheck.isChecked

                //It will appear as 0 if the user doesn't enter the value for price.
                if(updateJobPrice.text.isEmpty())
                    job.jobPrice = 0.0 else
                    job.jobPrice = updateJobPrice.text.toString().toDouble()
                MainActivity.dbHandler.addJob(this,job)

                clearEdits()
                updateJobTitle.requestFocus()

            }
        }

        btnCancel.setOnClickListener {

            clearEdits()
            finish()
        }



    }

    //Clear operation for the user to re-enter data
    private fun clearEdits() {

        updateJobTitle.text.clear()
        updateJobDescription.text.clear()
        updateJobPrice.text.clear()


    }
}