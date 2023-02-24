package com.talixmines.management.inventorymanager.services.strategy;

import com.talixmines.management.exception.ErrorCodes;
import com.talixmines.management.exception.InvalidOperationException;
import com.flickr4java.flickr.FlickrException;
import java.io.InputStream;

import com.talixmines.management.usermanager.services.strategy.SaveEntreprisePhoto;
import com.talixmines.management.usermanager.services.strategy.SaveUtilisateurPhoto;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyPhotoContext {

  private BeanFactory beanFactory;
  private Strategy strategy;
  @Setter
  private String context;

  @Autowired
  public StrategyPhotoContext(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public Object savePhoto(String context, Integer id, InputStream photo, String title) throws FlickrException {
    determinContext(context);
    return strategy.savePhoto(id, photo, title);
  }

  private void determinContext(String context) {
    final String beanName = context + "Strategy";
    switch (context) {
      case "article":
        strategy = beanFactory.getBean(beanName, SaveArticlePhoto.class);
        break;
      case "client" :
        strategy = beanFactory.getBean(beanName, SaveClientPhoto.class);
          break;
      case "fournisseur" :
        strategy = beanFactory.getBean(beanName, SaveFournisseurPhoto.class);
          break;
      case "entreprise" :
        strategy = beanFactory.getBean(beanName, SaveEntreprisePhoto.class);
          break;
      case "utilisateur" :
        strategy = beanFactory.getBean(beanName, SaveUtilisateurPhoto.class);
          break;
      default: throw new InvalidOperationException("Contexte inconnue pour l'enregistrement de la photo", ErrorCodes.UNKNOWN_CONTEXT);
    }
  }


}
