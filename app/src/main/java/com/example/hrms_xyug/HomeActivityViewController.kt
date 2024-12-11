package com.example.hrms_xyug

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.Interface.BussinessUnitModel
import com.example.Interface.Data
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.example.fragments.ChatFragment
import com.example.fragments.HomeFragment
import com.example.fragments.InboxFragment
import com.example.fragments.MyTeamFragment
import com.example.fragments.OrganizationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityViewController : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var imageViewAdmin: CircleImageView
    private lateinit var sideMenu: ImageView
    private lateinit var titleText: TextView
    private lateinit var companyName: TextView
    private lateinit var itfarm: TextView
    private lateinit var org: TextView
    private lateinit var myteamtitle: TextView
    private lateinit var location: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var flFragment: FrameLayout
    private lateinit var logout: FrameLayout
    private val homeFragment = HomeFragment()
    private val chatFragment = ChatFragment()
    private val inboxFragment = InboxFragment()
    private val myTeamFragment = MyTeamFragment()
    private val organizationFragment = OrganizationFragment()
    private lateinit var navigationView: NavigationView
    private var selectedView: Int = 0
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private val TAG = HomeActivityViewController::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_view_controller)
        init()
    }

    override fun onResume() {
        super.onResume()
       // fetchEmployeeData()
        bussinessUnit()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        flFragment = findViewById(R.id.flFragment)
        titleText = findViewById(R.id.tittletext)
        myteamtitle = findViewById(R.id.myteamtitle)
        org = findViewById(R.id.org)

        itfarm = findViewById(R.id.itfarm)
        location = findViewById(R.id.location)

        val bundle = Bundle()
        changeFragment(homeFragment, "home", null, bundle)
        selectedView = R.id.nav_home

        fetchEmployeeData()
        bussinessUnit()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.nav_home

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view1)
        navigationView.setNavigationItemSelectedListener(this)
        sideMenu = toolbar.findViewById(R.id.toolbar_icon)
        sideMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        logout = findViewById(R.id.logout)
        navigationView.setCheckedItem(R.id.hearder_profile1)

        val headerView: View = navigationView.getHeaderView(0)
        val headerImage: View = headerView.findViewById(R.id.hearder_profile1)
        val imageView: View = headerView.findViewById(R.id.imageView11)
        imageView.setOnClickListener {
            val intent = Intent(this, PersonalDetails::class.java)
            startActivity(intent)
        }

        val  progressBar: ProgressBar = headerView.findViewById(R.id.progressBar)
        val nav_header_title: View = headerView.findViewById(R.id.nav_header_title)
        val nav_header_org: View = headerView.findViewById(R.id.nav_header_org)
        val nav_header_location: View = headerView.findViewById(R.id.nav_header_location)

        val nav_header_it: View = headerView.findViewById(R.id.nav_header_it)


        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this, PersonalDetails::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_clock -> {
                    val intent = Intent(this, ClockInClockOutActivity::class.java)
                    startActivity(intent)
                }

            }

            val textColor = ContextCompat.getColor(this, R.color.icongrey)
            val textSizeInDp = 14
            val textSizeInPx = (textSizeInDp * resources.displayMetrics.density).toInt()
            val typeface = ResourcesCompat.getFont(this, R.font.mulish_medium)

            menuItem.title = SpannableString(menuItem.title).apply {
                setSpan(ForegroundColorSpan(textColor), 0, length, 0)
                setSpan(AbsoluteSizeSpan(textSizeInPx), 0, length, 0)
            }

            true
        }
    }

    private fun restoreClockInState() {

    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragment)

        if (currentFragment is HomeFragment) {
            finishAffinity()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, homeFragment)
                .commitAllowingStateLoss()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }


    fun updateBottomNavigationBasedOnId(id: String) {
        Log.d(TAG, "Received ID: $id")
        val itemId = when (id) {
            "general_id" -> {
                updateTitleText("Home")
                org.visibility = View.GONE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
                R.id.nav_home

            }
            else -> {
                updateTitleText("Home")
                org.visibility = View.GONE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
                R.id.nav_home
            }
        }
        updateBottomNavigation(itemId)
    }

    fun updateTitleText(title: String) {
        runOnUiThread {
            Log.d(TAG, "Updating title text to: $title")
            if (::titleText.isInitialized) {
                titleText.text = title
            } else {
                Log.d(TAG, "TitleText not initialized")
            }
        }
    }

    private fun updateBottomNavigation(selectedItemId: Int) {
        runOnUiThread {
            Log.d(TAG, "Updating Bottom Navigation to itemId: $selectedItemId")
            bottomNavigationView.post {
                bottomNavigationView.menu.findItem(selectedItemId)?.let {
                    it.isChecked = true
                }
                bottomNavigationView.requestLayout()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, homeFragment).commit()
                updateTitleText("Home")
                org.visibility = View.GONE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
              //  onBackPressed()
                return true
            }
            R.id.nav_Inbox -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, inboxFragment).commit()
                selectedView = R.id.nav_Inbox
                updateTitleText("Inbox")
                org.visibility = View.GONE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
                return true
            }
            //do fast
            R.id.nav_me -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, chatFragment).commit()
                selectedView = R.id.nav_me
                updateTitleText("Me")
                org.visibility = View.GONE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
                return true
            }
            R.id.nav_team -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, myTeamFragment).commit()
                selectedView = R.id.nav_team
                updateTitleText("Your Team")
                org.visibility = View.GONE
                myteamtitle.visibility = View.VISIBLE
                itfarm.visibility = View.GONE
                location.visibility = View.GONE
                return true
            }
            R.id.nav_org -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, organizationFragment).commit()
                selectedView = R.id.nav_org
                updateTitleText("")
                getBusinnessDetails()
                org.visibility = View.VISIBLE
                myteamtitle.visibility = View.GONE
                itfarm.visibility = View.VISIBLE
                location.visibility = View.VISIBLE
                return true
            }
        }
        return false
    }

//    private fun logout() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.activity_logout_alert)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.setCancelable(false)
//        dialog.show()
//        dialog.window?.setBackgroundDrawableResource(R.drawable.layout_cornerbg)
//
//        val cancel: TextView = dialog.findViewById(R.id.successno)
//        val ok: TextView = dialog.findViewById(R.id.successdone)
//
//        cancel.setOnClickListener {
//            dialog.cancel()
//        }
//
//        ok.setOnClickListener {
//            dialog.dismiss()
//            AccountUtils.clearAccessToken(applicationContext)
//            val imagePref = applicationContext.getSharedPreferences("MyPreferences", MODE_PRIVATE)
//            val imageEditor = imagePref.edit()
//            imageEditor.remove("image_url")
//            imageEditor.apply()
//            Log.i("Logout", "Cleared image URL from SharedPreferences")
//            val pref = applicationContext.getSharedPreferences("HRMS", MODE_PRIVATE)
//            val editor = pref.edit()
//            editor.clear()
//            editor.apply()
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            finish()
//        }
//    }

    private fun logout() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_logout_alert)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.layout_cornerbg)

        val cancel: TextView = dialog.findViewById(R.id.successno)
        val ok: TextView = dialog.findViewById(R.id.successdone)

        cancel.setOnClickListener {
            dialog.cancel()
        }

        ok.setOnClickListener {
            dialog.dismiss()

            // Clear access token and other user-specific data
            AccountUtils.clearAccessToken(applicationContext)

            // Clear the saved image URL
            val imagePref = applicationContext.getSharedPreferences("MyPreferences", MODE_PRIVATE)
            val imageEditor = imagePref.edit()
            imageEditor.remove("image_url")
            imageEditor.apply()
            Log.i("Logout", "Cleared image URL from SharedPreferences")

            // Clear HRMS preferences
            val pref = applicationContext.getSharedPreferences("HRMS", MODE_PRIVATE)
            val editor = pref.edit()
            editor.clear()
            editor.apply()

            // Reset Clock State preferences to ensure the new user sees the default clock state
//            val clockStatePrefs = applicationContext.getSharedPreferences("ClockStatePrefs", MODE_PRIVATE)
//            val clockStateEditor = clockStatePrefs.edit()
//            clockStateEditor.putBoolean("IS_CLOCKED_IN", false)  // Indicating not clocked in
//            clockStateEditor.apply()
//            Log.i("Logout", "Reset Clock State to default (not clocked in)")

            val clockStatePrefs = applicationContext.getSharedPreferences("ClockStatePrefs", MODE_PRIVATE)
            val clockStateEditor = clockStatePrefs.edit()
            clockStateEditor.putBoolean("IS_CLOCKED_IN", false) // Indicating not clocked in
            clockStateEditor.apply()


            // Navigate to login screen
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun getBusinnessDetails(){
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        val token = AccountUtils.getAccessToken(applicationContext)

        apiService.getBussinessUnit(token).enqueue(object : Callback<BussinessUnitModel> {
            override fun onResponse(call: Call<BussinessUnitModel>, response: Response<BussinessUnitModel>) {

                if (response.isSuccessful) {
                    val bussinessUnitModel = response.body()

                    bussinessUnitModel?.let {
                        org.text = it.businessName ?: "N/A"
                        itfarm.text = (it.bussType ?: "N/A")
                        location.text = it.busslocation ?: "N/A"
                    }
                } else {
                    Log.e(TAG, "Failed to fetch business data")
                }
            }

            override fun onFailure(call: Call<BussinessUnitModel>, t: Throwable) {

                Log.e(TAG, "Error fetching business data: ${t.message}")
            }
        })
    }

    private fun changeFragment(fragment: Fragment, title: String, fragToHide: Fragment?, bundle: Bundle?) {
        Log.d(TAG, "hello " + supportFragmentManager.backStackEntryCount)
        if (fragment != null && !fragment.isVisible) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragToHide?.let {
                fragmentTransaction.hide(it)
            }
            bundle?.let {
                fragment.arguments = it
            }
            if (!fragment.isAdded) {
                fragmentTransaction.add(R.id.flFragment, fragment, title).addToBackStack(title).commit()
            }
        }
    }

    private fun fetchEmployeeData() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        val token = AccountUtils.getAccessToken(applicationContext)

        apiService.getSingleEmployee(token).enqueue(object : Callback<SingleEmployeeModel> {
            override fun onResponse(call: Call<SingleEmployeeModel>, response: Response<SingleEmployeeModel>) {
//              hideRefreshDialog()

                if (response.isSuccessful) {
                    val employeeData = response.body()?.data
                    updateHeaderFields(employeeData)
                } else {
                    Log.e(TAG, "Failed to fetch employee data")
                }
            }

            override fun onFailure(call: Call<SingleEmployeeModel>, t: Throwable) {

                Log.e(TAG, "Error fetching employee data: ${t.message}")
            }
        })
    }

    // for unauthorization user only
//    private fun redirectToLogin() {
//        clearUserSession()
//
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
//    }

//    private fun clearUserSession() {
//        val sharedPreferences = getSharedPreferences("your_app_prefs", Context.MODE_PRIVATE)
//        sharedPreferences.edit().clear().apply()
//    }

    private fun bussinessUnit() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        val token = AccountUtils.getAccessToken(applicationContext)

        apiService.getBussinessUnit(token).enqueue(object : Callback<BussinessUnitModel> {
            override fun onResponse(call: Call<BussinessUnitModel>, response: Response<BussinessUnitModel>) {

                if (response.isSuccessful) {
                    val bussinessUnitModel = response.body()
                    updateHeaderField(bussinessUnitModel)
                } else {
                    Log.e(TAG, "Failed to fetch employee data")
                }
            }

            override fun onFailure(call: Call<BussinessUnitModel>, t: Throwable) {

                Log.e(TAG, "Error fetching employee data: ${t.message}")
            }
        })
    }

    private fun updateHeaderField(bussinessUnitModel: BussinessUnitModel?) {
        runOnUiThread {
            bussinessUnitModel?.let {
                findViewById<TextView>(R.id.nav_header_org).text = it.businessName
                findViewById<TextView>(R.id.nav_header_location).text = it.busslocation
                findViewById<TextView>(R.id.nav_header_it).text = it.bussType  + ", "
            }
        }
    }

    private fun updateHeaderFields(employeeData: Data?) {
        runOnUiThread {
            employeeData?.let {
//               hideRefreshDialog()
                val employeedata = it.employeedata
                findViewById<TextView>(R.id.nav_header_title).text = "Hello ${employeedata?.firstName}" + "!"
                val imageUrl = employeedata?.photo
                val imageView: ImageView = findViewById(R.id.imageView11)

                Picasso.get()
                    .load(imageUrl)
                    .into(imageView)
            }
        }
    }
}

