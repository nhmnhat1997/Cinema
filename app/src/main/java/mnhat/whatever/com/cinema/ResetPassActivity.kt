package mnhat.whatever.com.cinema

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reset_pass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassActivity : AppCompatActivity() {

    var util: Utility = Utility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)



        btn_Reset.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.background.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                    view.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    run {
                        if (util.checkEmail(et_Email.text.toString()) == 1) {
                            var t: Toast = Toast.makeText(this, "Vui lòng nhập email.", Toast.LENGTH_LONG)
                            t.view.setBackgroundColor(R.drawable.toast)
                            t.show()

                        } else if (util.checkEmail(et_Email.text.toString()) == 2) {
                            var t: Toast = Toast.makeText(this, "Email không đúng định dạng.", Toast.LENGTH_LONG)
                            t.view.setBackgroundColor(R.drawable.toast)
                            t.show()
                        } else {
                            resetPassword(this, et_Email.text.toString())
                        }
                    }
                    view.background.clearColorFilter()
                    view.invalidate()
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.background.clearColorFilter()
                    view.invalidate()
                }
            }
            return@setOnTouchListener true
        }

        btn_SignIn.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.background.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                    view.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    run {
                        finish()
                    }
                    view.background.clearColorFilter()
                    view.invalidate()
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.background.clearColorFilter()
                    view.invalidate()
                }
            }
            return@setOnTouchListener true
        }
    }

    fun resetPassword(ctx: Context, email: String) {
        var mAPIService: APIService = APIUtils.getAPIService();
        var loadDialog: ProgressDialog = ProgressDialog(ctx,R.style.AlertDialogCustom)
        loadDialog.setTitle("Loading")
        loadDialog.show()
        mAPIService.resetPass(email).enqueue(object : Callback<ResetPassData> {
            override fun onResponse(call: Call<ResetPassData>, response: Response<ResetPassData>) {
                if (response.isSuccessful) {
                    var t: Toast = Toast.makeText(ctx,"Đặt lại mật khẩu thành công.", Toast.LENGTH_LONG)
                    t.view.setBackgroundColor(R.drawable.toast)
                    t.show()
                    loadDialog.dismiss()
                    val intent = Intent(this@ResetPassActivity, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else{
                    var t: Toast = Toast.makeText(ctx,"Email không tồn tại. Vui lòng kiểm tra lại.", Toast.LENGTH_LONG)
                    t.view.setBackgroundColor(R.drawable.toast)
                    t.show()
                    loadDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ResetPassData>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
