package com.example.robot_arm_app



import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.report_layout.*
import okhttp3.OkHttpClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_layout)

              if (BuildConfig.DEBUG){
                  Stetho.initializeWithDefaults(this)
              }

         OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()



        btnAdd.setOnClickListener { view ->

            addRecord()
        }

        setupListofDataIntoRecyclerView()
    }

    /**
     * Function is used to show the list of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Function is used to get the Items List from the database table.
     */
    private fun getItemsList(): ArrayList<EmpModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DDHelper = DDHelper(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<EmpModelClass> = databaseHandler.viewEmployee()

        return empList
    }


    // ======================================
    @RequiresApi(Build.VERSION_CODES.O)
    val current  = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    @SuppressLint("NewApi")
    val formatted = current.format(formatter)


    //=======================================

    //Method for saving the employee records in database
    private fun addRecord() {
        val name = etName.text.toString()
        val email = etEmailId.text.toString()
        val num = etEmumber.text.toString()
        val dataT = "time: $formatted"
        val databaseHandler: DDHelper = DDHelper(this)
        if (!name.isEmpty() && !email.isEmpty() && !num.isEmpty()) {
            val status =

                databaseHandler.addEmployee(EmpModelClass(0, name, email , num , dataT ))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etEmailId.text.clear()
                etEmumber.text.clear()

                setupListofDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Method is used to show the Custom Dialog.
     */
    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateEmailId.setText(empModelClass.nameITM)
        updateDialog.etUpdateNumber.setText(empModelClass.NumITM)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()
            val num = updateDialog.etUpdateNumber.text.toString()


            val databaseHandler: DDHelper = DDHelper(this)

            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email , num ,formatted))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "field cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    /**
     * Method is used to show the Alert Dialog.
     */
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${empModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DDHelper = DDHelper(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", "" , "",""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}









/*
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_layout)
        val location1 = findViewById<EditText>(R.id.edtLocation)
        val itemName = findViewById<EditText>(R.id.edtIemPicked)
        val numItem = findViewById<EditText>(R.id.edtNumItems)

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
      val loc = LocalDateTime.of(year, month, day, hour , minute , second   )

        var sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var date=  sdf.format(Date())
        var dateA = "item Picked AT:   $loc"

        btnAddRec.setOnClickListener {
            val db = DBHelper(this , null)

        val locationA = edtLocation.text.toString()
         val itmName = edtIemPicked.text.toString()
         val numItm = edtNumItems.text
       // var arr = mutableListOf<String>("operation:Date and time: $dateA" ,"location: $locationA", "Item Picked: $itmName", "Quantity: $numItm")
      //  var adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr)



            //listView.adapter = adapter





      //  toControl.setOnClickListener { arr.add(dateA) }
          db.addDATA(locationA , itmName)
            Toast.makeText(this ,locationA + " added to database",  Toast.LENGTH_LONG).show()

}
  btnViewRec.setOnClickListener {

   val db =DBHelper(this , null)
   val cursor = db.getDATA()
   cursor!!.moveToFirst()
      txtView.append(cursor.getString(cursor.getColumnIndex(DBHelper.LOCATION_COL)) + "\n")
      txtView.append(cursor.getString(cursor.getColumnIndex(DBHelper.ITEMNAME_COL)) + "\n")
     while (cursor.moveToNext()){

         txtView.append(cursor.getString(cursor.getColumnIndex(DBHelper.LOCATION_COL)) + "\n")
         txtView.append(cursor.getString(cursor.getColumnIndex(DBHelper.ITEMNAME_COL)) + "\n")
     }
      cursor.close()
  }


    }

 */




