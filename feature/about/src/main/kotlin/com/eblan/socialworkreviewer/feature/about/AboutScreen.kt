package com.eblan.socialworkreviewer.feature.about

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr

@Composable
internal fun AboutScreen(modifier: Modifier = Modifier) {
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("about"),
    ) {
        Details(
            image = R.drawable.eblan,
            title = "Social Work Reviewer",
            name = "1.0",
            message = "Your best companion app in preparing for your Licensure Exam for Social Work",
            links = listOf(
                "https://github.com/JackEblan/SocialWorkReviewer", "www.google.com",
            ),
            onLinkCLick = {},
        )

        Spacer(modifier = Modifier.height(10.dp))

        Details(
            image = R.drawable.patrick,
            title = "Owned and Maintained by",
            name = "Patrick Allain Atilano",
            message = "Enjoy your life",
            links = listOf(
                "https://www.facebook.com/ItsMePatikok",
                "https://github.com/Patikok-Softworks",
            ),
            onLinkCLick = {},
        )

        Spacer(modifier = Modifier.height(10.dp))

        Details(
            image = R.drawable.eblan,
            title = "Developed by",
            name = "Eblan",
            message = "An idiot admires complexity. A genius admires simplicity",
            links = listOf(
                "https://www.facebook.com/profile.php?id=61560918532193",
                "https://github.com/JackEblan",
            ),
            onLinkCLick = {},
        )
    }
}

@Composable
private fun Details(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    title: String,
    name: String,
    message: String,
    links: List<String>,
    onLinkCLick: (String) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                bitmap = ImageBitmap.imageResource(id = image),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = title, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = name)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = message,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.height(10.dp))

            WebLinks(links = links, onLinkCLick = onLinkCLick)
        }
    }
}

@Composable
private fun WebLinks(
    modifier: Modifier = Modifier,
    links: List<String>,
    onLinkCLick: (String) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        links.forEach { link ->
            IconButton(
                onClick = {
                    onLinkCLick(link)
                },
            ) {
                Icon(
                    imageVector = Swr.Link,
                    contentDescription = "",
                )
            }
        }
    }
}