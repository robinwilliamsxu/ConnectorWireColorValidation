
/**
 * Copyright 2011 Mentor Graphics Corporation. All Rights Reserved. <p>
 * Recipients who obtain this code directly from Mentor Graphics use it solely
 * for internal purposes to serve as example Java or Java Script plugins. This
 * code may not be used in a commercial distribution. Recipients may duplicate
 * the code provided that all notices are fully reproduced with and remain in
 * the code. No part of this code may be modified, reproduced, translated, used,
 * distributed, disclosed or provided to third parties without the prior written
 * consent of Mentor Graphics, except as expressly authorized above. <p> THE
 * CODE IS MADE AVAILABLE "AS IS" WITHOUT WARRANTY OR SUPPORT OF ANY KIND.
 * MENTOR GRAPHICS OFFERS NO EXPRESS OR IMPLIED WARRANTIES AND SPECIFICALLY
 * DISCLAIMS ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR WARRANTY OF NON-INFRINGEMENT. IN NO EVENT SHALL MENTOR GRAPHICS OR ITS
 * LICENSORS BE LIABLE FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING LOST PROFITS OR SAVINGS) WHETHER BASED ON
 * CONTRACT, TORT OR ANY OTHER LEGAL THEORY, EVEN IF MENTOR GRAPHICS OR ITS
 * LICENSORS HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. <p>
 */
import com.mentor.chs.api.IXAbstractConductor;
import com.mentor.chs.api.IXAbstractPin;
import com.mentor.chs.api.IXAbstractPinList;
import com.mentor.chs.api.IXCavity;
import com.mentor.chs.api.IXConnector;
import com.mentor.chs.api.IXObject;
import com.mentor.chs.api.IXWire;
import com.mentor.chs.plugin.IXApplicationContext;
import com.mentor.chs.plugin.IXApplicationContextListener;
import com.mentor.chs.plugin.drc.IXDRCViolationReporter;
import com.mentor.chs.plugin.drc.IXDRCheck.Severity;
import com.mentor.chs.plugin.drc.IXDRCheckAdvancedConfiguration;
import com.mentor.chs.plugin.drc.IXDRCheckAdvancedConfiguration.RunningMode;
import com.mentor.chs.plugin.drc.IXHarnessDRCheck;
import com.mentor.chs.plugin.drc.IXLogicDRCheck;
import java.util.Iterator;
import java.util.Set;

public class ConnectorWireColorlValidation implements IXLogicDRCheck,IXHarnessDRCheck,IXDRCheckAdvancedConfiguration, IXApplicationContextListener {

    IXApplicationContext context;

    public void begin(IXDRCViolationReporter arg0) {
        // do nothing
    }

    // Check each wire in the design and check if any StartTerminalSpec or
    // EndTerminalSpec properties are missing.  If so, report them.
    public void check(IXDRCViolationReporter reporter, IXObject obj) {
        if (obj instanceof IXConnector) {
            IXConnector conn = (IXConnector) obj;
            Set<IXAbstractPin> Pinlist= conn.getPins();
            for(IXAbstractPin pin: Pinlist)
            {
                if(pin instanceof IXCavity)
                {
                   IXCavity cavity = (IXCavity) pin;
                   Set<IXAbstractConductor> wirelist= cavity.getConductors();
                   for(IXAbstractConductor Conductor : wirelist)
                    {
                        if(Conductor instanceof IXWire)
                        {
                            IXWire wire= (IXWire) Conductor; 
                            String wirecolor= wire.getAttribute("WireColor");
                            String wireName= wire.getAttribute("Name");
                            Iterator iterator=Pinlist.iterator();
                            while(iterator.hasNext()) 
                            {
                                IXAbstractPin pinCompare= (IXAbstractPin)iterator.next();
                                if( pinCompare instanceof IXCavity)
                                {    
                                    IXCavity cavityCompare = (IXCavity) pinCompare;
                                    Set<IXAbstractConductor> wirelistcompare= cavityCompare.getConductors();
                                    for(IXAbstractConductor ConductorCompare :wirelistcompare)
                                    {   
                                        if(ConductorCompare instanceof IXWire)
                                         {
                                         IXWire wireCompare= (IXWire) ConductorCompare; 
                                         String wireCompareColor= wireCompare.getAttribute("WireColor");
                                         String wireCompareName = wireCompare.getAttribute("Name");
                                         if(wirecolor.equals(wireCompareColor) && !wireName.equals(wireCompareName))
                                            {
                                            reporter.report(Severity.Warning, "Wire {0} has same wire color with {1}.",wire,wireCompare );
                                                                                      
                                            }


                                         }
                                   }
                                }
                            }
//                             for(IXAbstractConductor ConductorCompare : wirelist)
//                             {
//                                 
//                                 if(ConductorCompare instanceof IXWire)
//                                 {
//                                 IXWire wireCompare= (IXWire) ConductorCompare; 
//                                 String wireCompareColor= wireCompare.getAttribute("WireColor");
//                                 String wireCompareName = wireCompare.getAttribute("Name");
//                                
//                                    if(wirecolor.equals(wireCompareColor) && !wireName.equals(wireCompareName))
//                                    {
//                                    reporter.report(Severity.Warning, "Connector {0} has same wire color connected to this connector.",conn );
//                                    }
//                                 }
//                                 
//                             }
                        }
                    }
                }
            
            }
            
        }
    }


    public void end(IXDRCViolationReporter arg0) {
    }

    public Severity getDefaultSeverity() {
        return Severity.Warning;
    }

    public String getDescription() {
        return "Verify Wire Color on each wire.";
    }

    public String getName() {
        return "Wire Color Validation";
    }

    public String getVersion() {
        return "1.0";
    }

    public boolean getAvailability(RunningMode runningMode, String designAbstraction) {
        if (runningMode == RunningMode.BACKGROUND) {
            return true;
        } else if (runningMode == RunningMode.ONSAVE) {

            return true;
        } else {
            return false;
        }
    }

    public void setApplicationContext(IXApplicationContext context) {
        this.context = context;
    }

    public boolean getDefaultAvailability() {
        return true;
    }

    public Severity getSeverity(RunningMode runningMode, String designAbstraction) {

        return Severity.Warning;
    }
}
