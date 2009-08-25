/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.ui.viewer;

import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;
import java.awt.AWTKeyStroke;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import net.java.jveez.ui.widgets.IconButton;
import net.java.jveez.utils.MouseWheelAdapter;
import org.apache.log4j.Logger;

public class ViewerActions implements MouseGesturesListener, KeyListener {

    private static final Logger LOG = Logger.getLogger(ViewerActions.class);

    private ViewerPanel viewerPanel;
    private ViewerImagePanel viewerImagePanel;

    private MouseGestures mouseGestures;
    private Map<String, ViewerAction> gestureMap = new HashMap<String, ViewerAction>();
    private Map<AWTKeyStroke, ViewerAction> keystrokeMap = new HashMap<AWTKeyStroke, ViewerAction>();

    private final MouseWheelAdapter mouseWheelListener;

    public ViewerActions(ViewerPanel viewerPanel, ViewerImagePanel viewerImagePanel) {
        this.viewerPanel = viewerPanel;
        this.viewerImagePanel = viewerImagePanel;

        setupGestures();
        addMouseWheelListener();

        register(goToFirstAction);
        register(goToPreviousAction);
        register(goToNextAction);
        register(goToLastAction);
        register(rotateLeftAction);
        register(rotateRightAction);
        register(zoomInAction);
        register(zoomOutAction);
        register(resetZoomAction);
        register(toggleHighQualityAction);
        register(closeViewerAction);
        register(togglePictureInformationAction);
        mouseWheelListener = new MouseWheelAdapter(5.0f) {
            public void mouseWheelDown(MouseWheelEvent e) {
                zoomInAction.onExecute();
            }

            public void mouseWheelUp(MouseWheelEvent e) {
                zoomOutAction.onExecute();
            }
        };
    }

    public JComponent getControlButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(true);
        panel.add(createButton(goToFirstAction));
        panel.add(createButton(goToPreviousAction));
        panel.add(createButton(goToNextAction));
        panel.add(createButton(goToLastAction));
        panel.add(createButton(rotateLeftAction));
        panel.add(createButton(rotateRightAction));
        panel.add(createButton(zoomInAction));
        panel.add(createButton(zoomOutAction));
        panel.add(createButton(resetZoomAction));
        panel.add(createButton(toggleHighQualityAction));
        panel.add(createButton(togglePictureInformationAction));
        return panel;
    }

    private JButton createButton(final ViewerAction action) {
        JButton button = null;
        if (action.getImage() != null) {
            button = new IconButton(action.getImage());
        }
        else {
            button = new JButton(action.getName());
        }
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action.onExecute();
            }
        });
        button.setToolTipText(action.getDescription());
        // make sure this component will not consume key events ...
        button.setFocusable(false);
        return button;
    }

    public void addMouseWheelListener() {
        viewerImagePanel.addMouseWheelListener(mouseWheelListener);
    }
    
    public void removeMouseWheelListener() {
        viewerImagePanel.removeMouseWheelListener(mouseWheelListener);
    }

    private void register(ViewerAction viewerAction) {
        if (viewerAction.getMouseGesture() != null) {
            gestureMap.put(viewerAction.getMouseGesture(), viewerAction);
        }

        if (viewerAction.getKeyStroke() != null) {
            keystrokeMap.put(viewerAction.getKeyStroke(), viewerAction);
        }
    }

    // keystroke stuff

    public void keyPressed(KeyEvent e) {
        AWTKeyStroke keyStroke = KeyStroke.getAWTKeyStrokeForEvent(e);
        ViewerAction action = keystrokeMap.get(keyStroke);
        if (action != null) {
            LOG.debug(String.format("Executing action '%s' for keyStroke '%s'", action.getName(), action.getKeyStroke()));
            action.onExecute();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    // mouse gesture stuff

    private void setupGestures() {
        mouseGestures = new MouseGestures();
        mouseGestures.addMouseGesturesListener(this);
        viewerPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                activateMouseGesture(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                activateMouseGesture(false);
            }
        });
    }

    private void activateMouseGesture(boolean b) {
        LOG.debug("activateMouseGesture(" + b + ")");

        if (b) {
            mouseGestures.start();
        }
        else {
            mouseGestures.stop();
        }
    }

    public void processGesture(String gesture) {
        LOG.debug("processGesture()");

        ViewerAction action = gestureMap.get(gesture);
        if (action != null) {
            LOG.debug(String.format("Executing action '%s' for gesture '%s'", action.getName(), action.getMouseGesture()));
            action.onExecute();
        }
    }

    public void gestureMovementRecognized(String currentGesture) {
    }

    // actions

    private ViewerAction goToFirstAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3258407314045483059L;

        protected void onCreate() {
            setName("<<");
            setDescription("Go to first picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_BACK_SPACE, InputEvent.SHIFT_MASK));
            setImageResourceName("net/java/jveez/icons/first.png");
        }

        protected void onExecute() {
            viewerPanel.gotoFirst();
        }
    };

    private ViewerAction goToPreviousAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 4049634598843527986L;

        protected void onCreate() {
            setName("<");
            setDescription("Go to previous picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
            setMouseGesture("L");
            setImageResourceName("net/java/jveez/icons/previous.png");
        }

        protected void onExecute() {
            viewerPanel.gotoPrevious();
        }
    };

    private ViewerAction goToNextAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3257853194444157747L;

        protected void onCreate() {
            setName(">");
            setDescription("Go to next picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, 0));
            setMouseGesture("R");
            setImageResourceName("net/java/jveez/icons/next.png");
        }

        protected void onExecute() {
            viewerPanel.gotoNext();
        }
    };

    private ViewerAction goToLastAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3546923601874662708L;

        protected void onCreate() {
            setName(">>");
            setDescription("Go to last picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK));
            setImageResourceName("net/java/jveez/icons/last.png");
        }

        protected void onExecute() {
            viewerPanel.gotoLast();
        }
    };

    private ViewerAction rotateLeftAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 4049918255713824820L;

        protected void onCreate() {
            setName("R-");
            setDescription("Rotate picture to the left");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK));
            setMouseGesture("LD");
            setImageResourceName("net/java/jveez/icons/rotate-left.png");
        }

        protected void onExecute() {
            viewerImagePanel.rotateLeft();
        }
    };

    private ViewerAction rotateRightAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3904676093905940533L;

        protected void onCreate() {
            setName("R+");
            setDescription("Rotate picture to the right");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_R, 0));
            setMouseGesture("RD");
            setImageResourceName("net/java/jveez/icons/rotate-right.png");
        }

        public void onExecute() {
            viewerImagePanel.rotateRight();
        }
    };

    private ViewerAction zoomInAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 4049361898384602421L;

        protected void onCreate() {
            setName("Z+");
            setDescription("Zoom in into the picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
            setImageResourceName("net/java/jveez/icons/zoom-in.png");
        }

        public void onExecute() {
            viewerImagePanel.zoomIn();
        }
    };

    private ViewerAction zoomOutAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3617573790864061492L;

        protected void onCreate() {
            setName("Z-");
            setDescription("Zoom out from the picture");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
            setImageResourceName("net/java/jveez/icons/zoom-out.png");
        }

        public void onExecute() {
            viewerImagePanel.zoomOut();
        }
    };

    private ViewerAction resetZoomAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3256721775537632821L;

        protected void onCreate() {
            setName("Z/");
            setDescription("Reset zooming");
            setKeystroke(KeyStroke.getAWTKeyStroke("control pressed CONTROL"));
            setImageResourceName("net/java/jveez/icons/zoom-reset.png");
        }

        public void onExecute() {
            viewerImagePanel.resetState();
        }
    };

    private ViewerAction toggleHighQualityAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 4120853244000942136L;

        protected void onCreate() {
            setName("Q");
            setDescription("Toggle high rendering quality");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_Q, 0));
            setImageResourceName("net/java/jveez/icons/quality.png");
        }

        public void onExecute() {
            viewerImagePanel.setHighQuality(!viewerImagePanel.isHighQuality());
        }
    };

    private ViewerAction closeViewerAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3977298819790419249L;

        protected void onCreate() {
            setName("closeViewer");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_ESCAPE, 0));
            setMouseGesture("DR");
        }

        public void onExecute() {
            viewerPanel.closeViewer();
        }
    };

    private ViewerAction togglePictureInformationAction = new ViewerAction() {
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3258413923899553073L;

        protected void onCreate() {
            setName("displayPictureInformation");
            setKeystroke(KeyStroke.getAWTKeyStroke(KeyEvent.VK_I, 0));
            setDescription("Display information about this picture");
            // todo : make an icon for this button
            setImageResourceName("net/java/jveez/icons/info.png");
        }

        public void onExecute() {
            viewerPanel.toogleDisplayPictureInformation();
        }
    };
}
