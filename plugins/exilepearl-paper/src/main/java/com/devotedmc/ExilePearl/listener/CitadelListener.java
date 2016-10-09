package com.devotedmc.ExilePearl.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.devotedmc.ExilePearl.ExilePearlApi;
import com.devotedmc.ExilePearl.ExileRule;

import vg.civcraft.mc.citadel.events.ReinforcementDamageEvent;
import vg.civcraft.mc.citadel.reinforcement.PlayerReinforcement;

public class CitadelListener extends RuleListener {

	
	public CitadelListener(ExilePearlApi pearlApi) {
		super(pearlApi);
	}

	/**
	 * Prevents exiled players from damaging reinforcements
	 * @param e The event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onReinforcementDamage(ReinforcementDamageEvent e) {
		PlayerReinforcement r = (PlayerReinforcement)e.getReinforcement();
		if (r == null) {
			return;
		}
		
		if (r.canBypass(e.getPlayer())) {
			return;
		}		
		
		checkAndCancelRule(ExileRule.DAMAGE_REINFORCEMENT, e, e.getPlayer());
	}
}
