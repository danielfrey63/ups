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

package net.java.jveez.ui.viewer.anim;

public class ResetAnimation implements Animation {

    private long duration;
    private AnimatedComponent targetComponent;

    private AnimationState finalState = new AnimationState();
    private AnimationState initialState = new AnimationState();
    private AnimationState deltaState = new AnimationState();

    public ResetAnimation(AnimationState finalState, long duration, AnimatedComponent targetComponent) {
        this.finalState.set(finalState);
        this.duration = duration;
        this.targetComponent = targetComponent;
    }

    public void init() {
        initialState.set(targetComponent.getAnimationState());

        // try to minimize the rotation move ...
        if (initialState.r >= Math.PI) {
            initialState.r -= Math.PI * 2;
        }
        else if (initialState.r <= -Math.PI) {
            initialState.r += Math.PI * 2;
        }
        //

        if (duration > 0) {
            AnimationState.delta(initialState, finalState, duration, deltaState);
        }
    }

    public void perform(long time) {
        AnimationState targetState = targetComponent.getAnimationState();
        if (time >= duration) {
            targetState.set(finalState);
        }
        else {
            AnimationState.step(initialState, deltaState, time, targetState);
        }
    }

    public long duration() {
        return duration;
    }
}
