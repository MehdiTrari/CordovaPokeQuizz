import Foundation
import AudioToolbox
import Cordova

@objc(AdvVibration) class AdvVibration: CDVPlugin {
  @objc(vibratePattern:)
  func vibratePattern(command: CDVInvokedUrlCommand) {
    guard let pattern = command.argument(at: 0) as? [Int], !pattern.isEmpty else {
      self.commandDelegate?.send(CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid pattern"), callbackId: command.callbackId)
      return
    }

    DispatchQueue.global(qos: .userInitiated).async {
      for (idx, ms) in pattern.enumerated() {
        if idx % 2 == 0 {
          AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
        }
        Thread.sleep(forTimeInterval: Double(ms) / 1000.0)
      }
      self.commandDelegate?.send(CDVPluginResult(status: CDVCommandStatus_OK), callbackId: command.callbackId)
    }
  }
}
