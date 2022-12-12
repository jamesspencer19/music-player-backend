package com.backend.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import com.backend.entity.Playlist;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlaylistRepository implements PanacheRepository<Playlist> {
}
