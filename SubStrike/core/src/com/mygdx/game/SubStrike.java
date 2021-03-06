package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import state.GameStateManager;
import state.MenuState;
import state.SplashState;
import sprites.Submarine;

public class SubStrike extends ApplicationAdapter
{
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final String TITLE = "Sub Strike";

	private GameStateManager gsm;
	private SpriteBatch batch;

	private Music music;

	private boolean on = true;

	@Override
	public void create()
	{
		batch = new SpriteBatch();
		gsm = new GameStateManager();

		// Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new SplashState(gsm));

	}

	@Override
	public void render()
	{

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	/*
	 * @Override public void dispose () { batch.dispose(); }
	 */

}