
import javax.media.opengl.GLAutoDrawable;

//import com.sun.opengl.util.Animator;
//import com.sun.opengl.util.BufferUtil;
import com.jogamp.opengl.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
 
/*
 * Pour utiliser Opengl notre classe doit implémenter l'interface
 * GLEventListener. Cela va nous permettre d'utiliser les fonctionalités 
 * d'OpenGl avec le JPanel.
 * L'interface oblige a redefinir 4 méthodes:
 *  -- init() // Cette methode est appelée une fois ,elle va contenir l'initialisation
 *    -- display() 
 *        // Cette methode sera appélé en boucle et contiendra 
 *        // toute la partie affichage
 *    -- reshape() 
 *        // Cette methode est appelé quand il ya un redimensionnement de la fenêtre
 *        // afin d'eviter un affichage diforme
 *    -- displayChanged() 
 */
public class JOGLTest implements GLEventListener {
 
    private int large;
    private float rotateT = 0.0f;
    private static final GLU glu = new GLU();
    /*pour resoudre le probleme du buffer*/
    private float vertices[];
    private float couleurs[];
    private FloatBuffer verticesBuff = BufferUtil.newFloatBuffer(24 * 3);
    private FloatBuffer couleursBuff = BufferUtil.newFloatBuffer(24 * 3);
 
    /**
     * GLEventListener renvoie un contexte (drawable)
     * Qui va nous permettre d'instancier un objet
     * de type GL à partir du quel on utilisera
     * les fonctions OpenGL.
     * */
    public void init(GLAutoDrawable drawable) {
        // Instanciation de l'objet GL 
        final GL gl = drawable.getGL();
 
        /*
         * Désactive la synchronisation verticale
         * indépendement de la plate forme utilisée
         */
        gl.setSwapInterval(1);
 
 
        //gl.glShadeModel(GL.GL_SMOOTH);
 
        /* Methode qui etablit vers quel couleur la fenetre sera 
         * vidé en l'occurence le noir 
         */
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
 
 
        /*
         * indique la valeur de profondeur (entre 0.0 et 1.0) 
         * utilisée par glClear pour effacer le tampon de profondeur
         */
        gl.glClearDepth(1.0f);
 
        gl.glEnable(GL.GL_DEPTH_TEST);
 
        gl.glDepthFunc(GL.GL_LEQUAL);
        /*Really Nice Perspective Calculations*/
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
 
        //Tableau avec les coordonnées des sommets
        vertices = new float[]{
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f
        };
        couleurs = new float[]{
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
            1.5f, 0.5f, 0.0f,
            1.5f, 0.5f, 0.0f,
            1.5f, 0.5f, 0.0f,
            1.5f, 0.5f, 0.0f
        };
 
        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL.GL_COLOR_ARRAY);
 
 
        gl.glVertexPointer(3, GL.GL_FLOAT, 0, verticesBuff);
        gl.glColorPointer(3, GL.GL_FLOAT, 0, couleursBuff);
 
 
 
    }
 
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        final GL gl = drawable.getGL();
        if (height <= 0) {
            height = 1;
        }
        final float h = (float) width / (float) height;
 
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 2.0, 1000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
 
    public void display(GLAutoDrawable drawable) {
 
        GL gl = drawable.getGL();
 
        /*  glClear()
         *  efface les tampons à l'intérieur du "viewport
         *  glClear prend un seul argument qui est
         *  un masque de bits de plusieurs valeurs,
         *  chacune indiquant un tampon à effacer.
         *  
         *  GL_COLOR_BUFFER_BIT ==> Indique les tampons de couleurs 
         *                          actuellement utilisés en écriture.
         *  
         *  GL_DEPTH_BUFFER_BIT ==> Indique le tampon de profondeur 
         */
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
 
        // remplace la matrice courante par la matrice d'identité
        gl.glLoadIdentity();
 
        /*
         *Déplace l'origine du système de coordonnées au point spécifié 
         * La matrice courante est multipliée par cette matrice de translation
         * Si la matrice courante est soit GL_MODELVIEW soit GL_PROJECTION, tous 
         * les objets dessinés après l'appel à glTranslate subissent
         * cette translation.
         */
        gl.glTranslatef(0.0f, 0.0f, -5.0f);
 
        /*
         * calcule une matrice qui réalise la rotation d'angle degrés 
         * dans le sens trigonométrique autour du vecteur (x, y, z).
         * Meme principe que glTranslate
         */
        gl.glRotatef(300.0f, 250.0f, -155.0f, 115.0f);
        gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
 
 
        /*forcé sinon les buffer sont vide*/
        verticesBuff.put(vertices);
        couleursBuff.put(couleurs);
        verticesBuff.rewind();
        couleursBuff.rewind();
        // La ligne magique
        gl.glDrawArrays(GL.GL_QUADS, 0, 24);
        /*
         * Force l'execution des commandes non encore achevées
         */
        gl.glFlush();
        rotateT += 0.4f * large / 300;
 
 
 
    }
 
    public void displayChanged(GLAutoDrawable drawable,
            boolean modeChanged, boolean deviceChanged) {
    }
 
    public static void main(String[] args) {
 
        /*Creation d'une fenetre
         * Utilisation d'un composant swing
         */
        JFrame frame = new JFrame("Esssai de java openGl");
 
 
        /*Creation d'un paneau
         * Utilisation d'un composant swing plutot qu'awt(GLCanvas)
         * C'est le paneau sur lequel on dessine
         */
        GLJPanel panel = new GLJPanel();
 
        /* On attache un "ecouteur" a la surface dessinable qui va capturer 
         * les évenements de la classe TestJOGL
         */
        JOGLTest tmp = new JOGLTest();
        tmp.setLarge(400);
        panel.addGLEventListener(tmp);
        panel.repaint();
 
        /*
         * On ajoute le panneau a la fenetre
         * (layout par defaut (BorderLayout))
         */
        frame.add(panel);
 
 
        /*Creation de l'animator 
         * Il va gérer l'appel en boucle de la méthode display(), 
         * qui s'occupe de la creation de l'image 
         * L'animator crée un thread dans lequel les appels a display() 
         * sont effectués.Il va aussi se charger de faire 
         * une pause entre chaque rafraichissement de l'image pour 
         * permettre aux autres threads ou processus de s'executer.
         * On peut reduire la pause avec cette methode setRunAsFastAsPossible(true); 
         * Il possède deux méthodes principales : start() et stop()
         * pour lancer et arreter l'animation.
         */
        final Animator animator = new Animator(panel);
 
        /*
         * Bout de code habituel qui permet de fermer
         * la fenetre.Voir cours sur les api Graphiques
         */
        frame.addWindowListener(new WindowAdapter() {
 
            public void windowClosing(WindowEvent e) {
 
 
                // Creation d'un thread que se charge d'arreter l'annimator
                // avant l'arret du programme
                new Thread(new Runnable() {
 
                    public void run() {
                        animator.stop(); // stop l'affichage
                        System.exit(0);  // quite le programme
                    }
                }).start();
            }
        });
 
        /* 
         * Modifie la resolution de la fenetre
         */
        frame.setSize(400, 400);
 
        /*
         *Rend la fenetre visible 
         */
        frame.setVisible(true);
 
        /* 
         * Lance le thread qui se charge de l'affichage
         */
        animator.start();
    }
 
    public void setLarge(int large) {
        this.large = large;
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}