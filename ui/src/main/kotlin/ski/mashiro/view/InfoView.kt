package ski.mashiro.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ski.mashiro.common.GlobalBean

/**
 * @author mashirot
 * 2024/2/17 21:47
 */
@Composable
fun InfoView() {
    Column {
        Row {
            Text("Author: mashirot")
        }
        Row {
            Text("Version: ${GlobalBean.systemConfig.version}")
        }
        Row {
            Text("This software is protected by the terms of the GNU General Public License version 3")
        }
    }
}