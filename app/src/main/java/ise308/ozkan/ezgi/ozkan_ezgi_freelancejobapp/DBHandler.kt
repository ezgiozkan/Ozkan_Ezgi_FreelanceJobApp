package ise308.ozkan.ezgi.ozkan_ezgi_freelancejobapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import java.lang.Exception

class DBHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int ):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){



    //The columns that should be in the database

    companion object{

        private  val DATABASE_NAME = "MyData.db"
        private  val DATABASE_VERSION = 1
        val JOBS_TABLE_NAME = "Jobs"
        val COLUMN_JOBID = "jobid"
        val COLUMN_JOBTITLE = "jobtitle"
        val COLUMN_JOBDESCRIPTION = "jobdescription"
        val COLUMN_JOBPRICE = "jobprice"
        val COLUMN_DAYSLEFT = "daysleft"
        val COLUMN_JOBCHECK = "jobcheck"

    }


      //created a table named JOBS_TABLE_NAME and entered what type of value the columns will take into it.

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_JOBS_TABLE = ("CREATE TABLE $JOBS_TABLE_NAME (" +
                "$COLUMN_JOBID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_JOBTITLE TEXT," +
                "$COLUMN_JOBDESCRIPTION TEXT," +
                "$COLUMN_DAYSLEFT INT DEFAULT 0,"+
               "$COLUMN_JOBCHECK flag INT DEFAULT 0, " +
                "$COLUMN_JOBPRICE DOUBLE DEFAULT 0)")

        db?.execSQL(CREATE_JOBS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    //Function required to show the added data to the user
    fun getJobs(fCtx: Context): ArrayList<Freelancer>{

        //Query that will show all data in the JOBS TABLE_NAME table to the user.
        val query = "Select * From $JOBS_TABLE_NAME"
        val db = this.readableDatabase
        //Cursors are structures that bring each data in the data set to us step by
        // step and thus enable us to operate on a row basis
        val cursor = db.rawQuery(query, null)
        //Created an array called jobs that contains the data in the Freelancer class.
        val jobs = ArrayList<Freelancer>()

        //If there is no data in the database, a warning message is given to the user.

        if(cursor.count == 0)
            Toast.makeText(fCtx,"No records found", Toast.LENGTH_SHORT).show() else
        {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()) {
            val job = Freelancer()
            job.jobID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOBID))
            job.jobTitle = cursor.getString(cursor.getColumnIndex(COLUMN_JOBTITLE))
            job.jobDescription = cursor.getString(cursor.getColumnIndex(COLUMN_JOBDESCRIPTION))
            job.daysLeft = cursor.getInt(cursor.getColumnIndex(COLUMN_DAYSLEFT))
            job.jobPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_JOBPRICE))
                job.jobCheck = (cursor.getInt(cursor.getColumnIndex(COLUMN_JOBCHECK)) == 1)

                jobs.add(job)
                cursor.moveToNext()
        }

        }

        cursor.close()
        db.close()
        return jobs
    }

    //The data added by the user are saved in the database.
    fun addJob(fCtx: Context, freelancer: Freelancer) {
        val values = ContentValues()
        values.put(COLUMN_JOBTITLE, freelancer.jobTitle)
        //values.put(COLUMN_JOBID, freelancer.jobID)
        values.put(COLUMN_JOBDESCRIPTION, freelancer.jobDescription)
        values.put(COLUMN_DAYSLEFT, freelancer.daysLeft)
        values.put(COLUMN_JOBPRICE, freelancer.jobPrice )
        values.put(COLUMN_JOBCHECK,freelancer.jobCheck)

        val db = this.writableDatabase
        try {

            db.insert(JOBS_TABLE_NAME,null,values)
            Toast.makeText(fCtx,"Freelance Job added", Toast.LENGTH_SHORT).show()

        } catch (e: Exception){

            Toast.makeText(fCtx,e.message, Toast.LENGTH_SHORT).show()

        }
        db.close()
    }

    //If the user presses the delete button, the data is deleted.
    fun deleteJob(jobID: Int) : Boolean {

        //Database query required to delete selected data
        val query = "Delete From $JOBS_TABLE_NAME where $COLUMN_JOBID = $jobID"
        val db = this.writableDatabase
        var result: Boolean = false

        try {
           // val cursor = db.delete(JOBS_TABLE_NAME,"$COLUMN_JOBID = ?", arrayOf(jobID.toString()))
            val cursor = db.execSQL(query)
            result = true
        }catch (e: Exception){
           Log.e(ContentValues.TAG, "Error Deleting")
        }
       db.close()
        return  result
    }


    //If the user presses the edit button, a new dialog fragment opens and the data is updated.

    fun updateJob(id:String,jobTitle: String,jobDescription:String,jobPrice:String,daysLeft:String,jobCheck:String) : Boolean{

        val db= this.writableDatabase
        val contentValues = ContentValues()
        var result : Boolean = false
        contentValues.put(COLUMN_JOBTITLE,jobTitle)
        contentValues.put(COLUMN_JOBDESCRIPTION,jobDescription)
        contentValues.put(COLUMN_JOBPRICE,jobPrice.toDouble())
        contentValues.put(COLUMN_DAYSLEFT,daysLeft.toInt())
        contentValues.put(COLUMN_JOBCHECK,jobCheck.toBoolean())
        try {
            db.update(JOBS_TABLE_NAME,contentValues,"$COLUMN_JOBID = ? ", arrayOf(id) )
            result = true
        }catch (e:Exception){
            Log.e(ContentValues.TAG,"Error Updating")
            result = false
        }
            return result

    }

}