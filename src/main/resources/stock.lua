if (redis.call('exists',KEYS[1])==1) then
-- 如果这个key存在的话  获取的是传入值的 value  库存数量
    local stock=tonumber(redis.call('get',KEYS[1]));
    if(stock>0) then
        redis.call('incrby',KEYS[1],-1);
        return stock;
    end;
        return 0;
end;