import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

class Main
{
    public static void main(String[] args) {
        final Scope runtime = new Scope(null);
        Common.define(runtime);
        runtime.define("mouseX", 0f);
        runtime.define("mouseY", 0f);
        final Frame frame = new Frame();
        final Canvas canvas = new Canvas();
        canvas.setSize(600, 400);
        frame.setBackground(new Color(0xeeeeee));
        frame.setResizable(false);
        frame.add(canvas);
        canvas.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                runtime.set("mouseX", (float) e.getX());
                runtime.set("mouseY", (float) e.getY());
            }
            public void mouseMoved(MouseEvent e) {
                runtime.set("mouseX", (float) e.getX());
                runtime.set("mouseY", (float) e.getY());
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        final BufferStrategy strategy = canvas.getBufferStrategy();
        final Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        new Object() {
            int fps;
            Color background;
            Color stroke;
            Color fill;
            Stack<AffineTransform> stack;
            Function tick;
            {
                fps = 30;
                background = new Color(0xeeeeee);
                fill = Color.WHITE;
                stroke = Color.BLACK;
                stack = new Stack<>();
                runtime.define("background", new Function() {
                    Object call(Object[] arguments) {
                        int rgb = (int) (float) arguments[0];
                        if (background.getRGB() != rgb) {
                            background = new Color(rgb);
                        }
                        return null;
                    }
                });
                runtime.define("fps", new Function() {
                    Object call(Object[] arguments) {
                        fps = (int) (float) arguments[0];
                        return null;
                    }
                });
                runtime.define("title", new Function() {
                    Object call(Object[] arguments) {
                        frame.setTitle((String) arguments[0]);
                        return null;
                    }
                });
                runtime.define("fill", new Function() {
                    Object call(Object[] arguments) {
                        int rgb = (int) (float) arguments[0];
                        if (fill.getRGB() != rgb) {
                            fill = new Color(rgb);
                        }
                        return null;
                    }
                });
                runtime.define("stroke", new Function() {
                    Object call(Object[] arguments) {
                        int rgb = (int) (float) arguments[0];
                        if (stroke.getRGB() != rgb) {
                            stroke = new Color(rgb);
                        }
                        return null;
                    }
                });
                runtime.define("print", new Function() {
                    Object call(Object[] arguments) {
                        System.out.println(arguments[0]);
                        return null;
                    }
                });
                runtime.define("push", new Function() {
                    Object call(Object[] arguments) {
                        stack.push(g.getTransform());
                        return null;
                    }
                });
                runtime.define("pop", new Function() {
                    Object call(Object[] arguments) {
                        g.setTransform(stack.pop());
                        return null;
                    }
                });
                runtime.define("translate", new Function() {
                    Object call(Object[] arguments) {
                        g.translate((float) arguments[0], (float) arguments[1]);
                        return null;
                    }
                });
                runtime.define("scale", new Function() {
                    Object call(Object[] arguments) {
                        g.scale((float) arguments[0], (float) arguments[1]);
                        return null;
                    }
                });
                runtime.define("rotate", new Function() {
                    Object call(Object[] arguments) {
                        if (arguments.length == 1) {
                            g.rotate((float) arguments[0]);
                        } else {
                            g.rotate((float) arguments[0], (float) arguments[1], (float) arguments[2]);
                        }
                        return null;
                    }
                });
                runtime.define("clear", new Function() {
                    Object call(Object[] arguments) {
                        g.setColor(background);
                        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        return null;
                    }
                });
                runtime.define("rect", new Function() {
                    Object call(Object[] arguments) {
                        Shape shape = new Rectangle2D.Float(
                            (float) arguments[0],
                            (float) arguments[1],
                            (float) arguments[2],
                            (float) arguments[3]
                        );
                        g.setColor(fill);
                        g.fill(shape);
                        g.setColor(stroke);
                        g.draw(shape);
                        return null;
                    }
                });
                runtime.define("oval", new Function() {
                    Object call(Object[] arguments) {
                        Shape shape = new Ellipse2D.Float(
                            (float) arguments[0],
                            (float) arguments[1],
                            (float) arguments[2],
                            (float) arguments[3]
                        );
                        g.setColor(fill);
                        g.fill(shape);
                        g.setColor(stroke);
                        g.draw(shape);
                        return null;
                    }
                });
                runtime.define("circle", new Function() {
                    Object call(Object[] arguments) {
                        float x = (float) arguments[0];
                        float y = (float) arguments[1];
                        float r = (float) arguments[2];
                        Shape shape = new Ellipse2D.Float(
                            x - r,
                            y - r,
                            r * 2,
                            r * 2
                        );
                        g.setColor(fill);
                        g.fill(shape);
                        g.setColor(stroke);
                        g.draw(shape);
                        return null;
                    }
                });
                runtime.define("text", new Function() {
                    Object call(Object[] arguments) {
                        String text = (String) arguments[0];
                        float x = arguments.length > 1 ? (float) arguments[1] : 0;
                        float y = arguments.length > 2 ? (float) arguments[2] : 0;
                        g.setColor(fill);
                        g.drawString(text, x, y + g.getFontMetrics(g.getFont()).getAscent());
                        return null;
                    }
                });
                final Thread watcher = new Thread() {
                    {
                        setDaemon(true);
                        start();
                    }
                    public void run() {
                        try {
                            long lastMod = 0;
                            while (true) {
                                long newLastMod = lastMod("test.js");
                                if (lastMod != newLastMod) {
                                    lastMod = newLastMod;
                                    Statement program = Parsing.parse(read("test.js"));
                                    Scope global = new Scope(runtime);
                                    program.execute(global);
                                    tick = (Function) global.get("tick");
                                }
                                Thread.sleep(500);
                            }
                        } catch (Exception e) {
                        }
                    }
                };
                runtime.define("loadimage", new Function() {
                    Object call(final Object[] arguments) {
                        final Map image = new HashMap();
                        image.put("loaded", false);
                        image.put("width", 0f);
                        image.put("height", 0f);
                        new Thread() {
                            public void run() {
                                try {
                                    Image img = javax.imageio.ImageIO.read(new java.io.File((String) arguments[0]));
                                    image.put("image", img);
                                    image.put("width", (float) img.getWidth(null));
                                    image.put("height", (float) img.getHeight(null));
                                    image.put("loaded", true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        return image;
                    }
                });
                runtime.define("image", new Function() {
                    Object call(Object[] arguments) {
                        Map image = (Map) arguments[0];
                        if ((boolean) image.get("loaded")) {
                            int x = (int) (float) arguments[1];
                            int y = (int) (float) arguments[2];
                            g.drawImage((Image) image.get("image"), x, y, null);
                        }
                        return null;
                    }
                });
                final Thread looper = new Thread() {
                    {
                        setDaemon(true);
                        start();
                    }
                    public void run() {
                        try {
                            while (true) {
                                if (tick != null) {
                                    tick.call(new Object[0]);
                                    strategy.show();
                                }
                                Thread.sleep(1000 / fps);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        watcher.interrupt();
                        looper.interrupt();
                        e.getWindow().dispose();
                    }
                });
            }
        };
    }

    static String read(String file) {
        try {
            java.io.FileInputStream in = new java.io.FileInputStream(file);
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            while (true) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
            return new String(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    static long lastMod(String file) {
        try {
            return new java.io.File(file).lastModified();
        } catch (Exception e) {
            return 0;
        }
    }
}
