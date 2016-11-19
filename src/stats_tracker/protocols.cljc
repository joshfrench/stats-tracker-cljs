(ns stats-tracker.protocols)

(defprotocol ClassStats
  (ac [_])
  (pd [_])
  (md [_])
  (hp [_])
  (atk [_])
  (hit [_])
  (miss [_]))
