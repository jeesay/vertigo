/*
 * $Id:$
 *
 * crazybio_viewer: 3D Viewer Plugin for ImageJ.
 * Copyright (C) 2013  Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA, or see the FSF site: http://www.fsf.org.
 *
 * Authors :
 *Florin Buga
 *Olivier Catoliquot
 *Clement Delestre
 */

package vertigo.graphics.G2D;

import java.awt.Graphics;
import javax.swing.JPanel;

import vertigo.graphics.ViewportDispatcher;
import vertigo.graphics.event.ViewportSignal;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.World;


@SuppressWarnings("serial")
public class G2D_Panel extends JPanel {

    private Camera cam_;
    private World world_;
    private G2D_Visitor visitor;
    private G2D_Renderer renderer;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ViewportSignal signal = new ViewportSignal();
        signal.setSize(this.getWidth(),this.getHeight() );
        ViewportDispatcher.getInstance().fireUpdate(signal);
        world_.accept(visitor);
        renderer.setGraphicsContext(g);
        renderer.draw();
/*
    GradientPaint gp = new GradientPaint(0, 0, Color.RED, 30, 30, Color.cyan, true);
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
*/
// fps 60 ?
//        repaint();
  }

    void setWorld(World world) {
        world_ = world;
        cam_ = world_.getCamera();
        visitor = new G2D_Visitor();
        renderer = new G2D_Renderer();
        renderer.setWorld(world);
    }



}
