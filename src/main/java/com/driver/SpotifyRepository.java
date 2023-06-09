package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User (name,mobile)  ;
        users.add(user) ;
        return user  ;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name) ;
        artist.setLikes(0);
        artists.add(artist) ;
        return artist  ;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = null ;

        for (Artist art : artists){
            if (Objects.equals(art.getName() , artistName)){
                artist = art ;
                break ;
            }
        }

        if (artist == null){
            artist = createArtist(artistName) ;

            Album album = new Album(title)  ;
            album.setReleaseDate(new Date());

            albums.add(album) ;

            List<Album> albumL1 = new ArrayList <>() ;
            albumL1.add(album) ;

            artistAlbumMap.put(artist,albumL1) ;
            return album ;
        }
        else{
            Album album = new Album(title)  ;
            album.setReleaseDate(new Date ());

            albums.add(album) ;

            List <Album> albumL1 = new ArrayList<>() ;

            if (albumL1 == null){
                albumL1 = new ArrayList<>() ;
            }

            albumL1.add(album) ;
            artistAlbumMap.put(artist,albumL1) ;
            return album ;
        }
    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        Album album = null ;
        for (Album album1 : albums){
            if(album1.getTitle().equals(albumName)){
                album = album1 ;
                break ;
            }
        }

        if (album == null){
            throw new Exception("Album does not exist") ;
        }

        else{
            Song song = new Song() ;
            song.setTitle(title);
            song.setLength(length);
            song.setLikes(0);

            songs.add(song) ;

            if (albumSongMap.containsKey(album)){
                List <Song> songs1 = albumSongMap.get(album) ;
                songs1.add(song) ;
                albumSongMap.put(album,songs1) ;
            }
            else {
                List<Song> songList = new ArrayList<>() ;
                songList.add(song) ;
                albumSongMap.put(album,songList) ;
            }
            return song ;
        }
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = null ;

        for (User user1 : users){
            if(user1.getMobile() == mobile){
                user = user1 ;
                break ;
            }
        }

        if (user == null){
            throw new Exception("User does not exist") ;
        }

        else{
            Playlist playlist = new Playlist()  ;
            playlist.setTitle(title);
            playlists.add(playlist) ;

            List<Song> songList = new ArrayList<>() ;

            for (Song song : songs){
                if(song.getLength() == length){
                    songList.add(song) ;
                }
            }

            playlistSongMap.put(playlist , songList);

            List <User> users1 = new ArrayList<>() ;
            users1.add(user) ;
            creatorPlaylistMap.put(user,playlist) ;
            playlistListenerMap.put(playlist,users1) ;

            userPlaylistMap.get(user) ;

            if (userPlaylistMap.containsKey(user)){
                List<Playlist> userPlayList = userPlaylistMap.get(user) ;
                userPlayList.add(playlist) ;
                userPlaylistMap.put(user,userPlayList) ;
            }
            else{
                List<Playlist> playL = new ArrayList <>() ;
                playL.add(playlist) ;
                userPlaylistMap.put(user,playL) ;
            }
            return playlist ;
        }

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
              User user = null ;
              for (User user1 : users){
                  if (user1.getMobile() == mobile) {
                      user = user1;
                      break ;
                  }
              }

              if (user == null){
                  throw  new Exception("User does not exist") ;
              }
              else{
                  Playlist playlist = new Playlist() ;
                  playlist.setTitle(title);
                  playlists.add(playlist) ;

                  List <Song> songList = new ArrayList<>() ;
                  for (Song song : songs){
                      if(songTitles.contains(song.getTitle())){
                          songList.add(song)  ;
                      }
                  }
                  playlistSongMap.put(playlist,songList) ;

                  List <User> users1 = new ArrayList<>() ;
                  users1.add(user) ;

                  creatorPlaylistMap.put(user,playlist) ;
                  playlistListenerMap.put(playlist, users1) ;

                  if (userPlaylistMap.containsKey(user)){
                      List<Playlist> userPlaylist = userPlaylistMap.get(user) ;
                      userPlaylist.add(playlist) ;
                      userPlaylistMap.put(user,userPlaylist) ;
                  }
                  else{
                      List<Playlist> playlists1 = new ArrayList<>() ;
                      playlists1.add(playlist) ;
                      userPlaylistMap.put(user,playlists1) ;
                  }
                  return playlist ;
              }
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
          User user = new User() ;

          for (User user1 : users){
              if (user1.getMobile()== mobile){
                  user = user1 ;
                  break ;
              }
          }

          if (user == null){
              throw  new Exception("User does not exist") ;
          }

          Playlist playlist = null ;

          for (Playlist playlist1 : playlists){
              if(playlist1.getTitle() == playlistTitle){
                  playlist = playlist1 ;
                  break ;
              }
          }

          if  (playlist == null){
              throw new Exception("Playlist does not exist") ;
          }

          if (creatorPlaylistMap.containsKey(user)){
              return playlist ;
          }

          List<User>  users1 = playlistListenerMap.get(playlist);

          for (User user1 : users1){
              if (user1 == user){
                  return playlist  ;
              }
          }

          users1.add(user) ;
          playlistListenerMap.put(playlist,users1) ;

          List<Playlist> playlists1 = userPlaylistMap.get(user) ;

          if (playlists1 == null){
              playlists1 = new ArrayList<>() ;
          }

          playlists1.add(playlist) ;
          userPlaylistMap.put(user, playlists1) ;

          return playlist ;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = null ;
        for (User user1 : users){
            if (user1.getMobile() == mobile){
                user = user1 ;
                break;
            }
        }

        if (user == null){
            throw new Exception("User does not exist") ;
        }

        Song song = null ;

        for (Song song1 : songs){
            if(song1.getTitle() == songTitle){
                song = song1  ;
                break ;
            }
        }

        if (song == null){
            throw  new Exception("Song does not exist") ;
        }

        if (songLikeMap.containsKey(song)) {
            List<User> list = songLikeMap.get(song);
            if (list.contains(user)) {
                return song;
            } else {
                int likes = song.getLikes() +1 ;
                song.setLikes(likes);
                list.add(user) ;
                songLikeMap.put(song , list) ;

                Album album = null ;
                for (Album album1 : albumSongMap.keySet()){
                    List<Song>  songList = albumSongMap.get(album1) ;
                    if (songList.contains(song)){
                        album = album1 ;
                        break ;
                    }
                }
                Artist artist = null ;
                for (Artist artist1 : artistAlbumMap.keySet()){
                    List<Album> albumList = artistAlbumMap.get(artist1) ;
                    if (albumList.contains(album)){
                        artist = artist1 ;
                        break  ;
                    }
                }
                int likes1 = artist.getLikes() +1 ;
                artist.setLikes(likes1);
                artists.add(artist) ;
                return song ;
            }
        }
        else{
            int likes = song.getLikes()+1 ;
            song.setLikes(likes);
            List<User> list = new ArrayList<>() ;
            list.add(user) ;
            songLikeMap.put(song,list) ;

            Album album = null ;
            for (Album album1 : albumSongMap.keySet()){
                List<Song> songList = albumSongMap.get(album1) ;
                if(songList.contains(song)){
                    album = album1 ;
                    break ;
                }
            }
            Artist artist = null ;
            for (Artist artist1 : artistAlbumMap.keySet()){
                List<Album> albumList = artistAlbumMap.get(artist1) ;
                if (albumList.contains(album)){
                    artist = artist1 ;
                    break ;
                }
            }
            int likes1 = artist.getLikes()+1 ;
            artist.setLikes(likes1);
            artists.add(artist) ;

            return song ;
        }
    }

    public String mostPopularArtist() {
        int max = 0  ;
        Artist artist = null ;

        for (Artist artist1 : artists){
            if(artist1.getLikes()>=max){
                artist = artist1 ;
                max = artist1.getLikes() ;
            }
        }

        if (artist == null){
            return null ;
        }
        else{
            return artist.getName() ;
        }
    }

    public String mostPopularSong() {

        int max = 0 ;
        Song song = null ;

        for (Song song1 : songLikeMap.keySet()){
            if(song1.getLikes()>= max){
                song = song1 ;
                max = song1.getLikes();
            }
        }
        if(song == null){
            return null ;
        }
        else{
            return song.getTitle() ;
        }
    }
}
