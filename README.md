# Java Game Engine

![Banner](https://raw.githubusercontent.com/nurkert/java-gamengine/main/images/gamengine-banner.png)

A lightweight 2D game engine written in pure Java.  
Originally it grew inside the [Immune Till Death](https://github.com/nurkert/immune-till-death) project but is now extracted as a standalone library.

This repository contains the engine sources only.  The code below shows how the engine can be used to build a game similar to the original project.

## Features

- Basic rendering loop using `GFrame` and `GPanel`
- Flexible object system (`GObject` and subclasses)
- Event based input handling (`GEvent`/`GEventHandler`)
- Collision detection through spatial partitioning
- Simple particle and animation utilities
- Minimal dependencies (only the Java standard library)

## Building

Compile the sources with `javac`:

```bash
find src -name "*.java" > sources.txt
javac @sources.txt
```

This produces `.class` files next to the `.java` sources.  You can also import the project into your favourite IDE and build it there.

## Creating your own game

1. **Create a subclass of `GContent`** that will hold the objects of your world.  In the original project this class was named `World`.
2. **Create game entities** by extending `GEntity` or `GPlayer`.  Entities may override
   `handle`, `draw`, and the collision methods from `GObject.Collidable`.
3. **Register event listeners** using `GEventHandler.register(...)`.
4. **Create a `GFrame`** and set your `GContent` implementation as its content.
5. **Start the main thread** and place your initial objects.

Below is an excerpt adapted from the old *Immune Till Death* game.  It demonstrates how a world, a player and some viruses were spawned and how the event system was used.

```java
import eu.nurkert.gamengine.GFrame;
import eu.nurkert.gamengine.logic.GContent;
import eu.nurkert.gamengine.logic.GLocation;
import eu.nurkert.gamengine.logic.events.GEventHandler;
import eu.nurkert.gamengine.visual.GTexture;

public class Main implements GEventHandler.GEventListener {
    static World world;
    static Player player;
    static GFrame frame;

    public static void main(String[] args) {
        GEventHandler.register(new Main());

        frame = new GFrame("ImmuneTillDeath", new GContent.GDefaultContent());
        world = new World();
        frame.setContent(world);
        frame.setBackground(new GTexture("/textures/background.png"));

        // Spawn some enemies
        for (int i = 0; i < 50; i++) {
            Virus virus = new Random().nextBoolean() ?
                new Covid19(new GLocation(new Random().nextInt(1000) - 500,
                                          new Random().nextInt(1000) - 500,
                                          world), 7) :
                new Virus(new GLocation(new Random().nextInt(1000) - 500,
                                        new Random().nextInt(1000) - 500,
                                        world), 5);
            world.place(virus);
        }

        player = new Player(new GLocation(0, 0, world),
                            frame.getPanel().getContent().getViewCenter());
        world.setPlayer(player);
        world.place(player);

        world.place(new PlayerUI(player));
    }
}
```

Each entity overrides the `handle` method to update its behaviour.  The `World` class
invokes `place` to add objects and uses a `Timer` to spawn additional items over time.
User input is received through `KeyPressedGEvent` and `MouseClickGEvent` which are triggered automatically by the `GFrame`.

For detailed examples inspect the files under `src/eu/nurkert/gamengine` and adapt the code above to your own needs.  Textures referenced in the example must be available on the classpath.

## Directory Overview

- `src/eu/nurkert/gamengine` – the engine source code
- `images/` – artwork used in the README banner

## Contributing

Feel free to fork the project and experiment.  Pull requests that improve documentation or
provide bug fixes are welcome.

