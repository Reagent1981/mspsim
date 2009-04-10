/**
 * Copyright (c) 2007, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of MSPSim.
 *
 * $Id: WindowCommands.java 187 2008-03-17 19:34:12Z joxe $
 *
 * -----------------------------------------------------------------
 *
 * WindowCommands - 
 * 
 * Author  : Joakim Eriksson
 * Created : 9 april 2008
 * Updated : $Date: 2008-03-17 20:34:12 +0100 (Mon, 17 Mar 2008) $
 *           $Revision: 187 $
 */
package se.sics.mspsim.cli;

import java.util.Hashtable;

import se.sics.mspsim.ui.WindowUtils;
import se.sics.mspsim.util.ComponentRegistry;

public class WindowCommands implements CommandBundle {

  private Hashtable <String, WindowTarget> windowTargets = new Hashtable<String, WindowTarget>();

  public void setupCommands(ComponentRegistry registry, CommandHandler handler) {
    handler.registerCommand(">#", new BasicLineCommand(null, "<windowname>") {
      WindowTarget wt;
      public int executeCommand(CommandContext context) {
        String windowName = context.getArgument(0);
        wt = windowTargets.get(windowName);
        if (wt == null) {
          wt = new WindowTarget(windowName);
          windowTargets.put(windowName, wt);
        }
        return 0;
      }
      public void lineRead(String line) {
        wt.lineRead(line);
      }
      public void stopCommand(CommandContext context) {
        // Should this do anything?
        // Probably depending on the wt's config
        System.out.println("Stopping window target: " + wt.getName());
      }
    });
    
    handler.registerCommand("wclose", new BasicCommand("close the specified window", "<windowname>") {
      public int executeCommand(CommandContext context) {
        String name = context.getArgument(0);
        WindowTarget wt = windowTargets.get(name);     
        if (wt != null) {
          context.out.println("Closing window " + name);
          windowTargets.remove(name);
          wt.close();
          return 0;
        } else {
          context.err.println("Could not find the window " + name);
          return 1;
        }
      }
    });
    handler.registerCommand("wclear", new BasicCommand("resets stored window positions", "") {
      public int executeCommand(CommandContext context) {
        WindowUtils.clearState();
        return 0;
      }
    });
  }
}
