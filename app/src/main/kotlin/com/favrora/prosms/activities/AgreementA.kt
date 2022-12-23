package com.favrora.prosms.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.favrora.prosms.R
import com.favrora.prosms.helpers.PRIVACY_POLICY_URL
import com.favrora.prosms.helpers.Preference
import com.favrora.prosms.helpers.TERMS_OF_USE_URL
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.LinkBuilder

class AgreementA : SimpleActivity() {
    lateinit var tvGetStarted: TextView
    lateinit var tvTermsOfUse: TextView
    lateinit var chBox: CheckBox
    var links: ArrayList<Link> = ArrayList()
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        initControl()
        actionControl()
    }

    private fun actionControl() {
        tvGetStarted.setOnClickListener {
            if (chBox.isChecked)
            {
                Preference(this).setAgreementAccept(true)
                val intent=Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(context,context.getString(R.string.must_select_terms_and_condition), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initControl() {
        context=this
        chBox=findViewById(R.id.chBox)
        tvGetStarted=findViewById(R.id.tvGetStarted)
        tvTermsOfUse=findViewById(R.id.tvTermsOfUse)
        SetupScreenData()
    }

    private fun SetupScreenData() {
        val link1: Link = Link(context.getString(R.string.privacy_policy_))
        link1.setTextColor(ContextCompat.getColor(context, R.color.color_accent))
        link1.setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color.color_accent))
        link1.setUnderlined(true)
        link1.setBold(false)
        link1.setHighlightAlpha(.20f)
        link1.setOnClickListener(object: Link.OnClickListener {
            override fun onClick(clickedText: String) {
                openWebUrl(PRIVACY_POLICY_URL)

            }
        })
        val link: Link = Link(context.getString(R.string.terms_of_use))
        link.setTextColor(ContextCompat.getColor(context, R.color.color_accent))
        link.setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color.color_accent))
        link.setUnderlined(true)
        link.setBold(false)
        link.setHighlightAlpha(.20f)
        link.setOnClickListener(object: Link.OnClickListener {
            override fun onClick(clickedText: String) {
                openWebUrl(TERMS_OF_USE_URL)
            }
        })
        links.add(link1)
        links.add(link)
        val sequence = LinkBuilder.from(context, tvTermsOfUse.getText().toString())
            .addLinks(links)
            .build()
        tvTermsOfUse.setText(sequence)
        tvTermsOfUse.setMovementMethod(LinkMovementMethod.getInstance())
    }


    fun openWebUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}

