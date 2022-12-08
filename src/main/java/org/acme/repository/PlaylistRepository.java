package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entity.Playlist;
import org.acme.entity.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlaylistRepository implements PanacheRepository<Playlist> {
}
