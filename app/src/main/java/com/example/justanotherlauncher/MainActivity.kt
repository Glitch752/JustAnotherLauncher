package com.example.justanotherlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.DrawablePainter

class MainActivity : ComponentActivity() {
    private lateinit var appList: List<ResolveInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Transparent status and navigation bars
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Get a list of all installed apps
        appList = packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN,null).addCategory(Intent.CATEGORY_LAUNCHER),0)
        appList = appList.filter {
            // Exclude the launcher itself
            it.activityInfo.packageName != this.packageName
        }

        setContent {
            // Text(text = "App names: $appNames", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(20.dp))
            AppGridLayout(this.packageManager, appList, ::startActivity)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppGridLayout(packageManager: PackageManager, apps: List<ResolveInfo>, startActivity: (Intent) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).absolutePadding(10.dp, 150.dp, 10.dp, 200.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        apps.sortedWith(compareBy { app ->
            app.loadLabel(packageManager).toString()
        }).forEach { app ->
            AppIcon(app, packageManager, startActivity)
        }
    }
}

@Composable
fun AppIcon(app: ResolveInfo, packageManager: PackageManager, startActivity: (Intent) -> Unit) {
    Button(
        onClick = {
            val intent = Intent().apply {
                setClassName(app.activityInfo.packageName, app.activityInfo.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Transparent, disabledContentColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        // Turn off corner rounding
        shape = RectangleShape,
        modifier = Modifier.width(80.dp).padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(painter = DrawablePainter(app.loadIcon(packageManager)), contentDescription = "", modifier = Modifier.padding(6.dp).fillMaxWidth().aspectRatio(1f))
            Text(
                text = app.loadLabel(packageManager).toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 2.dp).fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontSize = 11.sp,
                softWrap = false,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}