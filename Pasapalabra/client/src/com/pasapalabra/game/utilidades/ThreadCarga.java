package com.pasapalabra.game.utilidades;

import com.pasapalabra.game.controllers.ThemeController;

public class ThreadCarga implements Runnable {

	ThemeController eventosJuego;

	public ThreadCarga(ThemeController eventosJuego) {
		this.eventosJuego = eventosJuego;
	}

	@Override
	public void run() {

	}
}