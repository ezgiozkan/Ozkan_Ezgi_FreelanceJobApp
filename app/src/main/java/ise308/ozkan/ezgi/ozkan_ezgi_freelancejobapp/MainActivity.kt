package ise308.ozkan.ezgi.ozkan_ezgi_freelancejobapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler

    }
    var jobList = ArrayList<Freelancer>()
    lateinit var adapter : RecyclerView.Adapter<*>
    lateinit var rv : RecyclerView


    //Custom tool bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this,null,null,1)

        viewJobs()

        editSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            //User can find job postings in the recycler view by searching by job title.

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               var filteredFreelanceJob = ArrayList<Freelancer>()
                if(!editSearch.text.isEmpty()){
                    for (i in 0 ..jobList.size - 1){

                        if(jobList.get(i).jobTitle!!.toLowerCase().contains(s.toString().toLowerCase()))

                            filteredFreelanceJob.add(jobList[i])
                    }
                    adapter = FreelancerAdapter(this@MainActivity, filteredFreelanceJob)
                    rv.adapter = adapter

                }else{ //if list is empty
                    adapter = FreelancerAdapter(this@MainActivity,jobList)
                    rv.adapter = adapter
                }

            }

        })
    }
    // Navigate to job_update_page layout

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.OptionItemSelected -> {

                val i = Intent(this,AddJobActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }



    @SuppressLint("WrongConstant")
    private fun viewJobs(){
         jobList = dbHandler.getJobs(this)
         adapter = FreelancerAdapter(this,jobList)
         rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false) as RecyclerView.LayoutManager
        rv.adapter = adapter

    }

    override fun onResume(){
        viewJobs()
        super.onResume()


    }


}