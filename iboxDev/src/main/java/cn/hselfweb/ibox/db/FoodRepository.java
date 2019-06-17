package cn.hselfweb.ibox.db;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface FoodRepository extends Repository<Food, Long> {
    Food getAllByFoodId(Long foodId);


    Food save(Food food);
}
