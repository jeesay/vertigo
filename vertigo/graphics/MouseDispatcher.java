/*
 * $Id:$
 *
 * vertigo: 3D Viewer Plugin for ImageJ.
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
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.graphics;

import java.util.ArrayList;
import java.util.Observable;
import vertigo.graphics.event.Signal;
import vertigo.scenegraph.Transform;

/**
 * Class MouseDispatcher
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class MouseDispatcher extends Observable {

    private static MouseDispatcher INSTANCE = new MouseDispatcher();

    private MouseDispatcher() {
        // Do nothing
    }

    /**
     * MouseDispatcher is a singleton.
     *
     * @return instance
     */
    public static MouseDispatcher getInstance() {
        return INSTANCE;
    }

    /**
     * Update an event.
     *
     * @param a signal
     */
    public void fireUpdate(Signal e) {
        setChanged();
        notifyObservers(e);
    }
} // end of class  Mouse Dispatcher
