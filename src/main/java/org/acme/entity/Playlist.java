package org.acme.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Entity
@Table(name = "playlists")
@JsonSerialize
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlistid")
    public int playlistid;
    @Column(name = "songids")
    public String songids;
    @Column(name = "username")
    private String username;

    public String getSongids() {
        return songids;
    }

    public void setSongids(String songids) {
        this.songids = songids;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
