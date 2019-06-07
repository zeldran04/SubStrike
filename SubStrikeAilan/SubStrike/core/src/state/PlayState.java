package state;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.SubStrike;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

import sprites.Fish;
import sprites.Submarine;
import sprites.Fish2;
import sprites.Fish3;
import sprites.Fish4;
import sprites.Fish5;
import sprites.Torpedo;
import sprites.Medkit;

public class PlayState extends State
{
	private Submarine sub;
	private Texture background;
	private Texture backgroundclouds;
	private Texture sun;
	private Texture hearts;
	private Fish fish;
	private TextureRegion imgTextureRegion;
	private int offset = 0;
	private double CamX = 0;
	private float CamX1 = -1;
	float sourceX = 0;
	float sourceX1 = 0;
	final int WIDTH = 7000;
	final int HEIGHT = 1080;
	private int renders = 0;
	private boolean leftmouse = false;
	private boolean lol = false;
	private Sound fire;
	private Sound hit1;
	private Sound hit2;
	private Sound hitM;
	private Sound hurt;
	private Sound minion1;
	private Sound minion2;
	private Sound healthregain;
	private Music music;
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	BitmapFont font;
	
	
	ArrayList<Fish> fishes = new ArrayList<Fish>();
	ArrayList<Fish2> fishes2 = new ArrayList<Fish2>();
	ArrayList<Fish3> fishes3 = new ArrayList<Fish3>();
	ArrayList<Fish4> fishes4 = new ArrayList<Fish4>();
	ArrayList<Fish5> fishes5 = new ArrayList<Fish5>();
	ArrayList<Medkit> medkit1 = new ArrayList<Medkit>();
	ArrayList<Torpedo> torpedos = new ArrayList<Torpedo>();

	// health bar
	private Texture fullHealth;
	private Texture health75;
	private Texture health50;
	private Texture health25;
	private Texture health0;
	private int health = 4;

	private float speed = 0;
	private float increment = 0;
	private double time = 0;
	private double score = 0;
	private double fishtime = 0;
	float newspeed = 0;
	BitmapFont scoreText;
	BitmapFont HighscoreText;
	Preferences prefs = Gdx.app.getPreferences("My Preferences");

	public PlayState(GameStateManager gsm)
	{
		super(gsm);
		sub = new Submarine(50, 200);
		cam.setToOrtho(false, SubStrike.WIDTH, SubStrike.HEIGHT);

		hearts = new Texture("Hearts.png");
		backgroundclouds = new Texture("Backgroundclouds1.png");
		background = new Texture("background.png");
		sun = new Texture("Sun.png");
		backgroundclouds.setWrap(Repeat, Repeat);
		background.setWrap(Repeat, Repeat);

		fire = Gdx.audio.newSound(Gdx.files.internal("Torpedo.mp3"));

		hit1 = Gdx.audio.newSound(Gdx.files.internal("hit1.mp3"));
		hit2 = Gdx.audio.newSound(Gdx.files.internal("hit2.mp3"));

		hitM = Gdx.audio.newSound(Gdx.files.internal("hitM.mp3"));

		hurt = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
		healthregain = Gdx.audio.newSound(Gdx.files.internal("healthregain.mp3"));

		minion1 = Gdx.audio.newSound(Gdx.files.internal("minion1.mp3"));
		minion2 = Gdx.audio.newSound(Gdx.files.internal("minion2.mp3"));

		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(1f);
		music.play();

		// health bar
		fullHealth = new Texture("100.png");
		health75 = new Texture("75.png");
		health50 = new Texture("50.png");
		health25 = new Texture("25.png");
		health0 = new Texture("0.png");

		scoreText = new BitmapFont();
		scoreText.getData().setScale(2, 2);

		HighscoreText = new BitmapFont();
		HighscoreText.setColor(Color.BLACK);
		HighscoreText.getData().setScale(2, 2);

		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("kongtext.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 35;
		
		fontParameter.color = Color.BLACK;
		font = fontGenerator.generateFont(fontParameter);
	}

	public void fishies()
	{
		int fishrand = 0;
		int powerRand = 0;
		int fish5rand = 0;
		int rand = 1 + (int) (Math.random() * 5);
		// System.out.println(rand);
		for (int loops = 0; loops < rand; loops++)
		{
			fishrand = 1 + (int) (Math.random() * 5);
			fish5rand = 1 + (int) (Math.random() * 5);
			powerRand = 1 + (int) (Math.random() * 10);

			// System.out.println(fish5rand);
			if (fishrand == 1)
			{
				int randX = 1800 + (int) (Math.random() * 500);
				fishes.add(new Fish(randX));
			} else
				if (fishrand == 2)
				{
					int randX = 1800 + (int) (Math.random() * 500);
					fishes2.add(new Fish2(randX));
				} else
					if (fishrand == 3)
					{
						int randX = 1800 + (int) (Math.random() * 500);
						fishes3.add(new Fish3(randX));
					} else
						if (fishrand == 4)
						{
							int randX = 1800 + (int) (Math.random() * 500);
							fishes4.add(new Fish4(randX));
						}

			if (fish5rand == 5)
			{
				int randX = 1800 + (int) (Math.random() * 500);
				fishes5.add(new Fish5(randX));
			}
			if (powerRand == 5)
			{
				int randX = 1800 + (int) (Math.random() * 500);
				medkit1.add(new Medkit(randX));
			}
		}

	}

	@Override
	public void handleInput()
	{
		if (Gdx.input.isKeyJustPressed(Input.Keys.M) && music.isPlaying())
		{
			music.pause();
		}

		else
			if (Gdx.input.isKeyJustPressed(Input.Keys.M) && !music.isPlaying())
			{
				music.play();
			}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			sub.jump();
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && leftmouse == false)
		{
			torpedos.add(new Torpedo(sub.getPosition().x, sub.getPosition().y + 80));
			leftmouse = true;
			fire.play(0.5f);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) == false && leftmouse == true)
		{
			leftmouse = false;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
		{
			torpedos.add(new Torpedo(sub.getPosition().x + 250, sub.getPosition().y + 80));
			fire.play(0.7f);
		}
	}

	@Override
	public void update(float dt)
	{
		handleInput();
		sub.update(dt);
		CamX -= 5;
		CamX1 -= 0.7;

		// fish 1
		for (int loops = 0; loops < fishes.size(); loops++)
		{
			Boolean removed = false;
			if ((sub.getPosition().x + 320 >= fishes.get(loops).getPosition().x
					&& (((sub.getPosition().y >= fishes.get(loops).getPosition().y - 40 && sub.getPosition().y <= fishes.get(loops).getPosition().y)))))
			{
				fishes.remove(loops);
				hurt.play(0.7f);
				// System.out.println("fuck");
				health -= 1;
				removed = true;
				break;
			}

			for (int Tloops = 0; Tloops < torpedos.size(); Tloops++)
			{
				if ((torpedos.get(Tloops).getPosition().x >= fishes.get(loops).getPosition().x
						&& torpedos.get(Tloops).getPosition().x <= fishes.get(loops).getPosition().x + fishes.get(loops).getFish().getRegionWidth())
						&& (torpedos.get(Tloops).getPosition().y >= fishes.get(loops).getPosition().y
								&& torpedos.get(Tloops).getPosition().y <= fishes.get(loops).getPosition().y + fishes.get(loops).getFish().getRegionHeight()))
				{
					hit2.play(0.5f);
					hitM.play(0.7f);
					torpedos.remove(Tloops);
					fishes.remove(loops);
					removed = true;
					score += 10;
					break;
				}
			}
			if (removed == false && (fishes.get(loops).getPosition().x <= -fishes.get(loops).getFish().getRegionWidth()))
			{
				fishes.remove(loops);
			} else
				if (removed == false)
				{
					fishes.get(loops).update(dt);
				}

		}

		// fish 2
		for (int loops = 0; loops < fishes2.size(); loops++)
		{
			Boolean removed = false;
			if ((sub.getPosition().x + 320 >= fishes2.get(loops).getPosition().x
					&& (((sub.getPosition().y >= fishes2.get(loops).getPosition().y - 40 && sub.getPosition().y <= fishes2.get(loops).getPosition().y)))))
			{
				fishes2.remove(loops);
				hurt.play(0.7f);
				// System.out.println("fuck");
				health -= 1;
				removed = true;
				break;
			}

			for (int Tloops = 0; Tloops < torpedos.size(); Tloops++)
			{
				if ((torpedos.get(Tloops).getPosition().x >= fishes2.get(loops).getPosition().x
						&& torpedos.get(Tloops).getPosition().x <= fishes2.get(loops).getPosition().x + fishes2.get(loops).getFish().getRegionWidth())
						&& (torpedos.get(Tloops).getPosition().y >= fishes2.get(loops).getPosition().y
								&& torpedos.get(Tloops).getPosition().y <= fishes2.get(loops).getPosition().y + fishes2.get(loops).getFish().getRegionHeight()))
				{
					hit1.play(0.5f);
					hitM.play(0.7f);
					torpedos.remove(Tloops);
					fishes2.remove(loops);
					removed = true;
					score += 10;
					break;
				}
			}
			if (removed == false && (fishes2.get(loops).getPosition().x <= -fishes2.get(loops).getFish().getRegionWidth()))
			{
				fishes2.remove(loops);
			} else
				if (removed == false)
				{
					fishes2.get(loops).update(dt);
				}

		}

		// fish 3
		for (int loops = 0; loops < fishes3.size(); loops++)
		{
			Boolean removed = false;
			if ((sub.getPosition().x + 320 >= fishes3.get(loops).getPosition().x
					&& (((sub.getPosition().y >= fishes3.get(loops).getPosition().y - 40 && sub.getPosition().y <= fishes3.get(loops).getPosition().y)))))
			{
				fishes3.remove(loops);
				hurt.play(0.7f);
				// System.out.println("fuck");
				health -= 1;
				removed = true;
				break;
			}

			for (int Tloops = 0; Tloops < torpedos.size(); Tloops++)
			{
				if ((torpedos.get(Tloops).getPosition().x >= fishes3.get(loops).getPosition().x
						&& torpedos.get(Tloops).getPosition().x <= fishes3.get(loops).getPosition().x + fishes3.get(loops).getFish().getRegionWidth())
						&& (torpedos.get(Tloops).getPosition().y >= fishes3.get(loops).getPosition().y
								&& torpedos.get(Tloops).getPosition().y <= fishes3.get(loops).getPosition().y + fishes3.get(loops).getFish().getRegionHeight()))
				{
					hit2.play(0.5f);
					hitM.play(0.7f);
					torpedos.remove(Tloops);
					fishes3.remove(loops);
					removed = true;
					score += 10;
					break;
				}
			}
			if (removed == false && (fishes3.get(loops).getPosition().x <= -fishes3.get(loops).getFish().getRegionWidth()))
			{
				fishes3.remove(loops);
			} else
				if (removed == false)
				{
					fishes3.get(loops).update(dt);
				}

		}

		// fish 4
		for (int loops = 0; loops < fishes4.size(); loops++)
		{
			Boolean removed = false;
			if ((sub.getPosition().x + 320 >= fishes4.get(loops).getPosition().x
					&& (((sub.getPosition().y >= fishes4.get(loops).getPosition().y - 40 && sub.getPosition().y <= fishes4.get(loops).getPosition().y)))))
			{
				fishes4.remove(loops);
				hurt.play(0.7f);
				// System.out.println("fuck");
				health -= 1;
				removed = true;
				break;
			}
			for (int Tloops = 0; Tloops < torpedos.size(); Tloops++)
			{
				if ((torpedos.get(Tloops).getPosition().x >= fishes4.get(loops).getPosition().x
						&& torpedos.get(Tloops).getPosition().x <= fishes4.get(loops).getPosition().x + fishes4.get(loops).getFish().getRegionWidth())
						&& (torpedos.get(Tloops).getPosition().y >= fishes4.get(loops).getPosition().y
								&& torpedos.get(Tloops).getPosition().y <= fishes4.get(loops).getPosition().y + fishes4.get(loops).getFish().getRegionHeight()))
				{
					hit1.play(0.5f);
					hitM.play(0.7f);
					torpedos.remove(Tloops);
					fishes4.remove(loops);
					removed = true;
					score += 10;
					break;
				}
			}
			if (removed == false && (fishes4.get(loops).getPosition().x <= -fishes4.get(loops).getFish().getRegionWidth()))
			{
				fishes4.remove(loops);
			} else
				if (removed == false)
				{
					fishes4.get(loops).update(dt);
				}

		}

		sub.update(dt);

		for (int loops = 0; loops < torpedos.size(); loops++)
		{

			if (torpedos.get(loops).getPosition().x >= torpedos.get(loops).getTorpedoText().getWidth() + 1800)
			{
				torpedos.remove(loops);
			} else
			{
				torpedos.get(loops).update(dt);
			}

		}

		// fish 5
		int minionRand;
		for (int loops = 0; loops < fishes5.size(); loops++)
		{
			Boolean removed = false;
			for (int Tloops = 0; Tloops < torpedos.size(); Tloops++)
			{
				if ((torpedos.get(Tloops).getPosition().x >= fishes5.get(loops).getPosition().x
						&& torpedos.get(Tloops).getPosition().x <= fishes5.get(loops).getPosition().x + fishes5.get(loops).getFish().getRegionWidth())
						&& (torpedos.get(Tloops).getPosition().y >= fishes5.get(loops).getPosition().y
								&& torpedos.get(Tloops).getPosition().y <= fishes5.get(loops).getPosition().y + fishes5.get(loops).getFish().getRegionHeight()))
				{
					hit2.play(0.5f);
					hitM.play(0.7f);
					minionRand = 1 + (int) (Math.random() * 2);
						if(minionRand == 1) {
							minion1.play(0.6f);
						}else if(minionRand == 2) {
							minion2.play(0.6f);
						}
					torpedos.remove(Tloops);
					fishes5.remove(loops);
					health -= 1;
					removed = true;
					break;
				}
			}
			if (removed == false && (fishes5.get(loops).getPosition().x <= -fishes5.get(loops).getFish().getRegionWidth()))
			{
				fishes5.remove(loops);
			} else
				if (removed == false)
				{
					fishes5.get(loops).update(dt);
				}

		}

		// medkit
		for (int loops = 0; loops < medkit1.size(); loops++)
		{
			Boolean removed = false;
			if ((sub.getPosition().x + 320 >= medkit1.get(loops).getPos().x && (((sub.getPosition().y >= medkit1.get(loops).getPos().y - 40 && sub.getPosition().y <= medkit1.get(loops).getPos().y)))))
			{
				if (health < 4)
				{
					medkit1.remove(loops);
					healthregain.play();

					health += 1;
					removed = true;
					break;
				}
			}

			if (removed == false && (medkit1.get(loops).getPos().x <= -medkit1.get(loops).getFish().getRegionWidth()))
			{
				medkit1.remove(loops);
			} else
				if (removed == false)
				{
					medkit1.get(loops).update(dt);
				}

		}

	}

	@Override
	public void render(SpriteBatch sb)
	{
		float before = sourceX;

		speed += 0.01;
		time += Gdx.graphics.getDeltaTime();
		score += Gdx.graphics.getDeltaTime();
		fishtime += Gdx.graphics.getDeltaTime();
		System.out.println("time = " + time);
		System.out.println("score = " + score);
		sourceX += 5;
		sourceX1 += 1;
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sourceX = (sourceX + Gdx.graphics.getDeltaTime() / 3 / 4) % background.getWidth();
		sourceX1 = (sourceX1 + Gdx.graphics.getDeltaTime() / 3 / 4) % background.getWidth();

		sb.draw(backgroundclouds, 0, 0, WIDTH, HEIGHT, (int) sourceX1, 0, background.getWidth(), background.getHeight(), false, false);
		sb.draw(background, 0, 0, WIDTH, HEIGHT, (int) sourceX, 0, background.getWidth(), background.getHeight(), false, false);
		sb.draw(sun, CamX1 + offset, 0);

		// health bar

		if (health >= 4)
		{
			health = 4;

		}
		if (health == 4)
		{
			sb.draw(fullHealth, 75, 975, fullHealth.getWidth(), fullHealth.getHeight());
		}

		if (health == 3)
		{
			// dispose();
			sb.draw(health75, 75, 975, fullHealth.getWidth(), fullHealth.getHeight());
		}

		if (health == 2)
		{
			// dispose();
			sb.draw(health50, 75, 975, fullHealth.getWidth(), fullHealth.getHeight());
		}

		if (health == 1)
		{
			// dispose();
			sb.draw(health25, 75, 975, fullHealth.getWidth(), fullHealth.getHeight());
		}

		if (health == 0)
		{
			// dispose();
			gsm.set(new MenuState(gsm));
			music.stop();
			sub.stopSounds();

			sb.draw(health0, 60, 950, fullHealth.getWidth(), fullHealth.getHeight());

			health = 4;

			if (prefs.contains("HIGHSCORE") == false)
			{

				prefs.putInteger("HIGHSCORE", (int) score);
				prefs.flush();
			} else
				if (prefs.getInteger("HIGHSCORE") < score)
				{

					prefs.putInteger("HIGHSCORE", (int) score);
					prefs.flush();
					System.out.println("Save: " + prefs.getInteger("HIGHSCORE"));
				}

		}

		if (renders == 120)
		{
			fishies();
			renders = 0;

		}
		for (int loops = 0; loops < torpedos.size(); loops++)
		{

			sb.draw(torpedos.get(loops).getTorpedoText(), torpedos.get(loops).getPosition().x, torpedos.get(loops).getPosition().y);
		}
		sb.draw(sub.getTexture(), sub.getPosition().x, sub.getPosition().y);

		for (int loops = 0; loops < fishes.size(); loops++)
		{
			sb.draw(fishes.get(loops).getFish(), fishes.get(loops).getPosition().x, fishes.get(loops).getPosition().y);
		}
		for (int loops = 0; loops < fishes2.size(); loops++)
		{
			sb.draw(fishes2.get(loops).getFish(), fishes2.get(loops).getPosition().x, fishes2.get(loops).getPosition().y);
		}
		for (int loops = 0; loops < fishes3.size(); loops++)
		{
			sb.draw(fishes3.get(loops).getFish(), fishes3.get(loops).getPosition().x, fishes3.get(loops).getPosition().y);
		}
		for (int loops = 0; loops < fishes4.size(); loops++)
		{
			sb.draw(fishes4.get(loops).getFish(), fishes4.get(loops).getPosition().x, fishes4.get(loops).getPosition().y);
		}
		for (int loops = 0; loops < fishes5.size(); loops++)
		{
			sb.draw(fishes5.get(loops).getFish(), fishes5.get(loops).getPosition().x, fishes5.get(loops).getPosition().y);
		}
		for (int loops = 0; loops < medkit1.size(); loops++)
		{
			sb.draw(medkit1.get(loops).getFish(), medkit1.get(loops).getPos().x, medkit1.get(loops).getPos().y);
		}
		font.draw(sb, "SCORE: " + (int) score, 1400, 950);
		if (prefs.contains("HIGHSCORE") == true)
		{
			font.draw(sb, "HIGHSCORE: " + prefs.getInteger("HIGHSCORE"), 1400, 1050);
		} else
		{
			font.draw(sb, "HIGHSCORE: " + (int) score, 1400, 1050);
		}

		renders++;
		sb.end();
	}

	@Override
	public void dispose()
	{

	}

}