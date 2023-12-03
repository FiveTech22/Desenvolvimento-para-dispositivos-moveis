package com.mycompany.confinance.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.ActivityMainBinding
import com.mycompany.confinance.databinding.CustomDialogExitAccountBinding
import com.mycompany.confinance.util.SharedPreferencesUtil
import com.mycompany.confinance.view.activity.expense.ExpenseActivity
import com.mycompany.confinance.view.activity.objective.ObjectiveActivity
import com.mycompany.confinance.view.activity.revenue.RevenueActivity
import com.mycompany.confinance.view.activity.user.CreateAccountActivity
import com.mycompany.confinance.view.activity.user.UserProfileActivity
import com.mycompany.confinance.view.company.AboutUsActivity
import com.mycompany.confinance.view.company.TermsOfUseActivity
import com.mycompany.confinance.viewmodel.HomeViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: HomeViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private var dialog: AlertDialog? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        styleFontMenu(navView)
        handleView(navView)
        observe()
        updateMonthYearText()
        checkMonthAndYear()
        handleClick()
    }

    override fun onResume() {
        super.onResume()
        closeMenuIfOpen()
    }

    private fun closeMenuIfOpen() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun handleView(navView: NavigationView) {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_renevue -> {
                    startActivity(Intent(this, RevenueActivity::class.java))
                    true
                }

                R.id.nav_graphic -> {
                    startActivity(Intent(this, GraphicActivity::class.java))
                    true
                }

                R.id.nav_about_us -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    true
                }

                R.id.nav_expense -> {
                    startActivity(Intent(this, ExpenseActivity::class.java))
                    true
                }

                R.id.nav_terms_of_use -> {
                    startActivity(Intent(this, TermsOfUseActivity::class.java))
                    true
                }

                R.id.nav_objective -> {
                    startActivity(Intent(this, ObjectiveActivity::class.java))
                    true
                }

                R.id.nav_exit -> {
                    handleDialogCustom()
                    true
                }

                else -> false
            }
        }
    }

    private fun handleDialogCustom() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding = CustomDialogExitAccountBinding.inflate(LayoutInflater.from(this))

        dialogBinding.buttonYesExit.setOnClickListener {
            dialog?.dismiss()
            SharedPreferencesUtil.saveUserId(this, 0)
            startActivity(
                Intent(this, CreateAccountActivity::class.java)
            )
        }

        dialogBinding.buttonNo.setOnClickListener {
            dialog?.dismiss()
        }

        dialog = build.setView(dialogBinding.root).create()
        dialog?.show()

    }


    private fun handleClick() {
        val navView: NavigationView = binding.navView
        val imageViewHeader = navView.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
        val img = SharedPreferencesUtil.getImg(context = applicationContext)
        if (img != 1) {
            when (img) {
                2 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_4)
                }

                3 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_3)
                }

                4 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_2)
                }

                5 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_1)
                }

                6 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_h1)
                }

                7 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_m1)
                }

                8 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_m2)
                }

                9 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_h2)
                }

                10 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_h3)
                }

                11 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_6)
                }

                12 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_7)
                }

                13 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_8)
                }

                14 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_9)
                }

                15 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_10)
                }

                16 -> {

                    imageViewHeader.setImageResource(R.drawable.perfil_11)
                }

                17 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_12)
                }

                18 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_13)
                }

                19 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_14)
                }

                20 -> {
                    imageViewHeader.setImageResource(R.drawable.perfil_15)
                }
            }

            imageViewHeader.setOnClickListener {
                startActivity(Intent(this, UserProfileActivity::class.java))
            }

        }
    }

    private fun formatarNumero(numero: Double): String {
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        formato.isGroupingUsed = true
        formato.groupingSize = 3

        return "R$ " + formato.format(numero)
    }

    private fun observe() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.appBarMain.shimmerLayoutTextTotal.shimmerColor = 0
            } else {
                binding.appBarMain.shimmerLayoutTextTotal.startLayoutAnimation()
            }
        }
        viewModel.totalBalance.observe(this) { total ->
            binding.appBarMain.textTotal.text = formatarNumero(total)
        }
    }

    private fun styleFontMenu(navView: NavigationView) {
        val myMenu = navView.menu
        val planItem = myMenu.findItem(R.id.plan)
        val sPlan = SpannableString(planItem.title)
        sPlan.setSpan(
            TextAppearanceSpan(this, R.style.NavigationItemAppearance),
            0,
            sPlan.length,
            0
        )
        planItem.title = sPlan

        val analyzeItem = myMenu.findItem(R.id.analyze)
        val sAnalyze = SpannableString(analyzeItem.title)
        sAnalyze.setSpan(
            TextAppearanceSpan(this, R.style.NavigationItemAppearance),
            0,
            sAnalyze.length,
            0
        )
        analyzeItem.title = sAnalyze


        val homeItem = myMenu.findItem(R.id.nav_home)
        val sHome = SpannableString(homeItem.title)
        sHome.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sHome.length,
            0
        )
        homeItem.title = sHome

        val objectiveItem = myMenu.findItem(R.id.nav_objective)
        val sObjective = SpannableString(objectiveItem.title)
        sObjective.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sObjective.length,
            0
        )

        objectiveItem.title = sObjective


        val revenueItem = myMenu.findItem(R.id.nav_renevue)
        val sRevenue = SpannableString(revenueItem.title)
        sRevenue.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sRevenue.length,
            0
        )
        revenueItem.title = sRevenue

        val expenseItem = myMenu.findItem(R.id.nav_expense)
        val sExpense = SpannableString(expenseItem.title)
        sExpense.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sExpense.length,
            0
        )
        expenseItem.title = sExpense

        val graphicItem = myMenu.findItem(R.id.nav_graphic)
        val sgraphic = SpannableString(graphicItem.title)
        sgraphic.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sgraphic.length,
            0
        )
        graphicItem.title = sgraphic

        val aboutItem = myMenu.findItem(R.id.nav_about_us)
        val sAbout = SpannableString(aboutItem.title)
        sAbout.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sAbout.length,
            0
        )
        aboutItem.title = sAbout

        val termsItem = myMenu.findItem(R.id.nav_terms_of_use)
        val sTerms = SpannableString(termsItem.title)
        sTerms.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sTerms.length,
            0
        )
        termsItem.title = sTerms

        val exitItem = myMenu.findItem(R.id.nav_exit)
        val sExit = SpannableString(exitItem.title)
        sExit.setSpan(
            TextAppearanceSpan(this, R.style.NavigationSubTitleAppearance),
            0,
            sExit.length,
            0
        )
        exitItem.title = sExit

    }

    private fun checkMonthAndYear() {
        binding.appBarMain.arrowBack.setOnClickListener {
            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                calendar.add(Calendar.YEAR, -1)
                calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            } else {
                calendar.add(Calendar.MONTH, -1)
            }
            updateMonthYearText()
            handleQuery()
        }

        binding.appBarMain.arrowNext.setOnClickListener {
            if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                calendar.add(Calendar.YEAR, 1)
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
            } else {
                calendar.add(Calendar.MONTH, 1)
            }
            updateMonthYearText()
            handleQuery()
        }
    }


    private fun updateMonthYearText() {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)

        val displayedYear = calendar.get(Calendar.YEAR)
        val displayedMonth = calendar.get(Calendar.MONTH)

        val monthArray = resources.getStringArray(R.array.months)
        val monthAbbreviationsArray = resources.getStringArray(R.array.month_abbreviations)

        val formattedDate = if (displayedYear != currentYear) {
            "${monthAbbreviationsArray[displayedMonth]}/${displayedYear % 100}"
        } else {
            monthArray[displayedMonth]
        }


        binding.appBarMain.textMonth.text = formattedDate
        handleQuery()
    }

    private fun handleQuery() {
        val displayedDate = binding.appBarMain.textMonth.text.toString()
        if (displayedDate.isNotEmpty()) {
            val monthAbbreviationsArray = resources.getStringArray(R.array.month_abbreviations)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonthAbbreviation = monthAbbreviationsArray[Calendar.getInstance().get(Calendar.MONTH)]
            val monthIndex = monthAbbreviationsArray.indexOfFirst {
                it.equals(displayedDate.substring(0, 3), ignoreCase = true)
            }

            if (monthIndex >= 0) {
                val yearString = if (displayedDate.length == 7) {
                    val selectedYear = displayedDate.substring(4)
                    val formattedYear = if (selectedYear.length == 2) {
                        "20$selectedYear"
                    } else {
                        selectedYear
                    }
                    formattedYear
                } else {
                    if (displayedDate.substring(0, 3).equals(currentMonthAbbreviation, ignoreCase = true)) {
                        currentYear.toString()
                    } else {
                        displayedDate.substring(4)
                    }
                }

                val year = try {
                    if (yearString.length == 2) {
                        val yw = "20${yearString}"
                        yw.toInt()

                    } else {
                        yearString.toInt()
                    }
                } catch (e: NumberFormatException) {
                    currentYear
                }

                val month = monthIndex + 1
                viewModel.queryMonthAndYear(month, year)
            }
        }
    }

}