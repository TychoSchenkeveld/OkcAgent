package org.ddosolitary.okcagent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import java.io.OutputStream

const val NOTIFICATION_CHANNEL_SERVICE = "agent_services"

fun showError(context: Context, msg: String) {
	context.startActivity(Intent(context, ErrorDialogActivity::class.java).apply {
		flags = Intent.FLAG_ACTIVITY_NEW_TASK
		putExtra(EXTRA_ERROR_MESSAGE, msg)
	})
}

fun showError(context: Context, resId: Int) = showError(context, context.getString(resId))

fun createNotificationChannel(context: Context) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val channel = NotificationChannel(
			NOTIFICATION_CHANNEL_SERVICE,
			context.getString(R.string.channel_service),
			NotificationManager.IMPORTANCE_MIN
		)
		(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
			.createNotificationChannel(channel)
	}
}

fun writeString(output: OutputStream, str: String) {
	val strBuf = str.toByteArray(Charsets.UTF_8)
	val lenBuf = byteArrayOf(
		(strBuf.size shr 8).toByte(),
		(strBuf.size and Byte.MAX_VALUE.toInt()).toByte()
	)
	output.write(lenBuf)
	output.write(strBuf)
	output.flush()
}
