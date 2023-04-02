package com.example.appli_musique

import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.appli_musique.adapter.MusiqueAdapter
import com.example.appli_musique.model.MusiqueModel
import java.text.FieldPosition
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

//--------------------------------------------------------------
//                                  DECLARATION VARIABLES GLOBALE
    lateinit var mediaPlayer : MediaPlayer
    var positionMusique = 0
    lateinit var ivPlay : ImageView
    lateinit var imgMusic : ImageView
    lateinit var tvTitreMusique : TextView
    val rawUser = arrayOf(R.raw.benson_boone_in_the_stars,R.raw.beyonce_cuff_it,R.raw.david_guetta_im_good,R.raw.dermot_kennedy_kiss_me,R.raw.ed_sheeran_celestial,R.raw.lil_nas_x_star_walkin,R.raw.maneskin_the_loneliest,R.raw.miley_cyrus_flowers,R.raw.pierre_un_jour_je_marierai_un_ange,R.raw.ridsa_nous_deux,R.raw.rihanna_lift_me_up,R.raw.sam_smith_unholy,R.raw.taylor_swift_anti_hero,R.raw.vianney_ed_sheeran_call_on_me)

//---------------------------------------------------------------
//                        BOUCLE DE JEU PRINCIPALE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialisation des variables une fois que l'activite a été creer
        ivPlay = findViewById<ImageView>(R.id.imgPlay)
        imgMusic = findViewById<ImageView>(R.id.imgMusique)
        tvTitreMusique = findViewById<TextView>(R.id.textView)

        selection_musique()
    }

//---------------------------------------------------------------
//                           SELECTION DE LA MUSIQUE

    fun selection_musique() {
        val listView = findViewById<ListView>(R.id.lvMusiques)

        val playlist = generateMusic()

        Log.d("duree", " ${playlist[0].duree.toString()}")

        var adapter = MusiqueAdapter(this, playlist)

        mediaPlayer = MediaPlayer.create(this, rawUser[0])

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            positionMusique = 0
            ivPlay.setImageResource(android.R.drawable.ic_media_pause)

            imgMusic.setImageResource(playlist[position].image)
            tvTitreMusique.text = playlist[position].nom

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, rawUser[position])
            mediaPlayer.start()


            suivant(position)
            precedent(position)
            Play_Pause(position)
            en_boucle(position)

            progress()
        }
    }


//                          CONTROLES UTILISATEUR
    fun Play_Pause(position: Int)
    {
        ivPlay.setOnClickListener()
        {
            if(mediaPlayer.isPlaying)
            {
                ivPlay.setImageResource(android.R.drawable.ic_media_play)
                mediaPlayer.pause()
            }
            else
            {
                ivPlay.setImageResource(android.R.drawable.ic_media_pause)
                mediaPlayer.start()
            }
        }


    }

    fun suivant(position : Int)
    {
        var imgsuivant = findViewById<ImageView>(R.id.imgSuivant)
        imgsuivant.setOnClickListener(){
            positionMusique +=1
            // si on est sur la derniere musique on passe a la premiere

            if ((positionMusique + position) > rawUser.size - 1)
            {
                positionMusique = -position
            }

            if(mediaPlayer.isPlaying)
            {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this,rawUser[position+positionMusique])
            mediaPlayer.start()

            imgMusic.setImageResource(generateMusic()[position+positionMusique].image)
            tvTitreMusique.text = generateMusic()[position+positionMusique].nom
        }
    }

    fun precedent(position : Int)
    {
        var imgprecedent = findViewById<ImageView>(R.id.imgPrecedent)
        imgprecedent.setOnClickListener(){
            positionMusique -=1
            // si on est sur la premiere musique on passe a la derniere
            if ((position + positionMusique) < 0)
            {
                positionMusique = rawUser.size - position - 1
            }

            if(mediaPlayer.isPlaying)
            {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this,rawUser[position+positionMusique])
            mediaPlayer.start()

            imgMusic.setImageResource(generateMusic()[position+positionMusique].image)
            tvTitreMusique.text = generateMusic()[position+positionMusique].nom
        }
    }

   fun en_boucle(position: Int) {
       val btnLoop = findViewById<ImageView>(R.id.imgBoucle)
       var boucle = 0
       btnLoop.setOnClickListener()
       {

           if (boucle>1)
               boucle = 0
           when(boucle){
               0 ->{
                   val Toast = Toast.makeText(applicationContext, "Loop activated on ${generateMusic()[position].nom}", Toast.LENGTH_SHORT).show()
               }
               1->{
                   val Toast = Toast.makeText(applicationContext, "Loop desactivated", Toast.LENGTH_SHORT).show()
               }
           }
           boucle++
           mediaPlayer.setLooping(!mediaPlayer.isLooping)
       }
   }


//---------------------------------------------------------------
//                      GESTION ET UTILISATION DE LA SEEK BAR

    fun progress() {
        val sbDureeMusique = findViewById<SeekBar>(R.id.sbDureeMusique)
        sbDureeMusique.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if (fromUser) mediaPlayer.seekTo(progress)
           }
           override fun onStartTrackingTouch(seekBar: SeekBar?) {
           }
           override fun onStopTrackingTouch(seekBar: SeekBar?) {
           }
        })
        initializeSeekBar(sbDureeMusique)
    }

    fun initializeSeekBar(seekBar: SeekBar)
    {
        seekBar.max = mediaPlayer.duration

        var handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 0)
                }catch (e :java.lang.Exception)
                {
                    seekBar.progress = 0
                }
            }
        },0)
    }


//---------------------------------------------------------------
//                      CREATION DE LA LISTE
    fun generateMusic() : ArrayList<MusiqueModel>
    {
            return arrayListOf(
            MusiqueModel(
                R.drawable.benson_boone_in_the_stars,
                "Benson Boone in the stars",
                "2.56"
                ),
            MusiqueModel(
                R.drawable.beyonce_cuff_it,
                "beyonce cuff it",
                "3.46"
            ),
            MusiqueModel(
                R.drawable.david_guetta_im_good,
                "David Guetta im good",
                "2.55"
            ),
            MusiqueModel(
                R.drawable.dermot_kennedy_kiss_me,
                "Dermot Kennedy kiss me",
                "3.55"
            ),
            MusiqueModel(
                R.drawable.ed_sheeran_celestial,
                "Ed Sheeran celestial",
                "3.30"
            ),
            MusiqueModel(
                R.drawable.lil_nas_x_star_walkin,
                "Lil Nas X star walkin",
                "3.33"
            ),
            MusiqueModel(
                R.drawable.maneskin_the_loneliest,
                "Maneskin the loneliest",
                "4.07"
            ),
            MusiqueModel(
                R.drawable.miley_cyrus_flowers,
                "Miley Cyrus flowers",
                "3.21"
            ),
            MusiqueModel(
                R.drawable.pierre_un_jour_je_marierai_un_ange,
                "Pierre de Maere un jour je marierai un ange",
                "2.42"
            ),
            MusiqueModel(
                R.drawable.ridsa_nous_deux,
                "Ridsa nous deux",
                "3.04"
            ),
            MusiqueModel(
                R.drawable.rihanna_lift_me_up,
                "Rihanna lift me up",
                "3.32"
            ),
            MusiqueModel(
                R.drawable.sam_smith_unholy,
                "Sam smith unholy",
                "2.37"
            ),
            MusiqueModel(
                R.drawable.taylor_swift_anti_hero,
                "Taylor Swift anti hero",
                "3.20"
            ),
            MusiqueModel(
                R.drawable.vianney_ed_sheeran_call_on_me,
                "Vianney et EDSheeran call on me",
                "3.20"
            )
        )
    }
}