package com.alchera.game.Structure.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.alchera.game.Structure.Utils.Variables.*;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;

/**
 * Created by Inspix on 21/09/2015.
 */
public class SoundManager {

    private static SoundManager ourInstance;
    private HashMap<String,Sound> sounds;
    private HashMap<String,Music> songs;
    private Music currentlyPlaying;

    private SoundManager() {
        this.songs = new HashMap<String, Music>();
        this.sounds = new HashMap<String, Sound>();
        this.loadSounds();
        this.loadSongs();
    }

    private void loadSounds() {
        this.sounds.put(Sounds.JUMP, Gdx.audio.newSound(Gdx.files.internal("sounds/jump.ogg")));
        this.sounds.put(Sounds.DIE, Gdx.audio.newSound(Gdx.files.internal("sounds/die.ogg")));
        this.sounds.put(Sounds.KEYPICKUP, Gdx.audio.newSound(Gdx.files.internal("sounds/keypickup.ogg")));
        this.sounds.put(Sounds.MENUBACK, Gdx.audio.newSound(Gdx.files.internal("sounds/menuback.ogg")));
        this.sounds.put(Sounds.MENUCHANGE, Gdx.audio.newSound(Gdx.files.internal("sounds/menuchange.ogg")));
        this.sounds.put(Sounds.MENUSELECT, Gdx.audio.newSound(Gdx.files.internal("sounds/menuselect.ogg")));
        this.sounds.put(Sounds.OPENDOOR, Gdx.audio.newSound(Gdx.files.internal("sounds/opendoor.ogg")));
        this.sounds.put(Sounds.SUCCESS, Gdx.audio.newSound(Gdx.files.internal("sounds/success.ogg")));
    }

    private void loadSongs() {
        this.songs.put(Songs.GAMEOVER, Gdx.audio.newMusic(Gdx.files.internal("music/gameover.ogg")));
        this.songs.put(Songs.GAMEPLAY, Gdx.audio.newMusic(Gdx.files.internal("music/gameplay.ogg")));
        this.songs.put(Songs.INTRO, Gdx.audio.newMusic(Gdx.files.internal("music/intro.ogg")));
    }

    public long playSound(String sound){
        return this.playSound(sound, 1f);
    }

    public long playSound(String sound,float volume){
        return this.playSound(sound, volume, 1f);
    }
    public long playSound(String sound,float volume,float pitch){
        return this.playSound(sound, volume, pitch, 0f);
    }
    public long playSound(String sound,float volume,float pitch, float pan){
        Sound s = this.sounds.get(sound);
        if (s == null){
            System.err.println("The sound with ID '" + sound + "' does not exist..");
            return -1;
        }
        return s.play(volume, pitch, pan);
    }

    public void stopSound(String sound){
        stopSound(sound,-1);
    }

    public void stopSound(String sound, long id){
        Sound s = this.sounds.get(sound);
        if (s == null){
            System.err.println("The sound with ID '" + sound + "' does not exist..");
            return;
        }
        if (id == -1)
            s.stop();
        else
            s.stop(id);
    }


    public void playSong(String song){
        this.playSong(song, 1f);
    }
    public void playSong(String song,float volume){
        this.playSong(song, volume, 1f);
    }
    public void playSong(String song,float volume,float pan){
       this.playSong(song, volume, pan, false);
    }

    public void playSongLooping(String song){
        this.playSongLooping(song, 1f);
    }
    public void playSongLooping(String song,float volume){
        this.playSongLooping(song, volume, 0f);
    }
    public void playSongLooping(String song,float volume,float pan){
        this.playSong(song, volume, pan, true);
    }

    private void playSong(String song,float volume,float pan,boolean looping){
        Music s = this.songs.get(song);
        if (s == null){
            System.err.println("The song with ID '" + song + "' does not exist..");
            return;
        }
        if (currentlyPlaying != null && currentlyPlaying != s)
            currentlyPlaying.stop();
        s.setLooping(looping);
        s.setPan(pan,volume);
        s.play();
        currentlyPlaying = s;
    }

    public void setSongVolume(float volume){
        currentlyPlaying.setVolume(MathUtils.clamp(volume,0,1));
    }

    public void changeSongVolume(float amount){
        currentlyPlaying.setVolume(MathUtils.clamp(currentlyPlaying.getVolume() + amount,0,1));
    }

    public void pauseSong(){
        currentlyPlaying.pause();
    }

    public void pauseSong(String song){
        Music s = this.songs.get(song);
        if (s == null){
            System.err.println("The song with ID '" + song + "' does not exist..");
            return;
        }
        s.pause();
    }

    public void stopSong(String song){
        Music s = this.songs.get(song);
        if (s == null){
            System.err.println("The song with ID '" + song + "' does not exist..");
            return;
        }
        s.stop();
    }

    public static SoundManager getInstance() {
        if (ourInstance == null)
            ourInstance = new SoundManager();
        return ourInstance;
    }
}
