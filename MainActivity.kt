package com.doucement.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.widget.*

class MainActivity : Activity() {
    private val purple = Color.rgb(111, 94, 220)
    private val ink = Color.rgb(34, 37, 56)
    private val pale = Color.rgb(245, 244, 252)
    private var calls = 0
    private val logs = mutableListOf<String>()
    private lateinit var root: LinearLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); home() }
    private fun base(title: String) {
        root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(24,26,24,24); setBackgroundColor(pale) }
        setContentView(ScrollView(this).apply { addView(root) })
        text("✳ doucement",27,purple,true); text(title,23,ink,true)
    }
    private fun text(s:String,size:Int=15,color:Int=ink,bold:Boolean=false)=TextView(this).apply {
        text=s;textSize=size.toFloat();setTextColor(color);if(bold)setTypeface(null,1);setPadding(0,9,0,9);root.addView(this)
    }
    private fun button(s:String, action:()->Unit) { root.addView(Button(this).apply { text=s;setTextColor(Color.WHITE);setBackgroundColor(purple);setOnClickListener{action()} }) }
    private fun field(hint:String,value:String="")=EditText(this).apply { this.hint=hint;setText(value);setPadding(14,10,14,10);setBackgroundColor(Color.WHITE);root.addView(this) }
    private fun home() {
        base("Votre assistant, à votre rythme")
        text("Un agent vocal qui peut prendre le relais lorsque répondre au téléphone devient difficile.",16)
        text("Prototype interactif · appels simulés",12,Color.GRAY)
        text("Niveaux d'assistance",18,ink,true)
        listOf("1 · Prendre un message","2 · Demander qui appelle","3 · Filtrer les appels","4 · Répondre à votre place","5 · Transférer à un proche").forEach{ text(it) }
        button("Tester une conversation"){ conversation() }
        button("Configurer le renvoi d'appel"){ forwarding() }
        button("Historique ($calls)"){ history() }
        button("Réglages de l'agent"){ settings() }
    }
    private fun conversation() {
        base("Conversation de démonstration")
        status=text("Agent : Bonjour. Vous êtes bien chez Doucement. Comment puis-je vous aider ?",16,purple,true)
        val input=field("Votre réponse…")
        button("Envoyer") {
            val user=input.text.toString().trim()
            if(user.isNotEmpty()) {
                val answer=when {
                    user.contains("rendez",true)||user.contains("réservation",true)->"Je peux noter votre demande. Quel jour et quelle heure vous conviennent ?"
                    user.contains("message",true)||user.contains("rappeler",true)->"Je peux transmettre un message. Quel est votre nom et votre numéro ?"
                    user.contains("urgence",true)->"Si danger immédiat, contactez les services d'urgence. Je peux aussi vous aider à joindre un proche."
                    user.contains("merci",true)->"Merci pour votre appel. Bonne journée !"
                    else->"Je comprends. Pouvez-vous préciser votre demande ou me laisser un message ?"
                }
                status.text="Vous : $user\n\nDoucement : $answer";logs.add(status.text.toString());calls++;input.setText("")
            }
        }
        button("Terminer"){home()}
    }
    private fun forwarding() {
        base("Renvoi d'appel")
        text("Le renvoi s'active auprès de votre opérateur. Les codes varient selon le pays et le forfait.")
        text("Numéro de destination",16,ink,true);val number=field("Ex. +1 514 555 0100")
        button("Afficher le numéro saisi"){Toast.makeText(this,number.text,Toast.LENGTH_LONG).show()}
        text("Cette version ne peut pas activer le renvoi directement sur votre ligne.",12,Color.GRAY)
        button("Retour"){home()}
    }
    private fun settings() {
        base("Réglages de l'agent");text("Nom de l'agent");field("Doucement","Doucement")
        text("Message d'accueil");field("Bonjour, vous êtes bien chez Doucement.","Bonjour, vous êtes bien chez Doucement. Comment puis-je vous aider aujourd'hui ?")
        button("Enregistrer"){Toast.makeText(this,"Réglages conservés pour la session",Toast.LENGTH_SHORT).show()}
        button("Retour"){home()}
    }
    private fun history() {
        base("Historique des appels");if(logs.isEmpty())text("Aucun appel pour le moment.")
        logs.reversed().forEach{text(it,14)};button("Retour"){home()}
    }
}
