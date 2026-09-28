package com.jpmc.midascore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;

/**
 *
 * @author Camacho
 */

@RestController
public class BalanceController {
    private final DatabaseConduit databaseConduit;
    
    public BalanceController(DatabaseConduit databaseConduit){
        this.databaseConduit = databaseConduit;
    }
    
    @GetMapping("/balance")
    public Balance user(@RequestParam Long userId){
        UserRecord user = databaseConduit.findUserById(userId);
        
        if (user == null){
            return new Balance(0);
        }
        
        return new Balance(user.getBalance());
    }
}
