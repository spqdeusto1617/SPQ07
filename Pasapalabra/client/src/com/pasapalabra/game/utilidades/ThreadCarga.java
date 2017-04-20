package com.pasapalabra.game.utilidades;

import com.pasapalabra.game.controllers.EventosJuego;

public class ThreadCarga implements Runnable {

	EventosJuego eventosJuego;

	public ThreadCarga(EventosJuego eventosJuego) {
		this.eventosJuego = eventosJuego;
	}

	@Override
	public void run() {

	}
}