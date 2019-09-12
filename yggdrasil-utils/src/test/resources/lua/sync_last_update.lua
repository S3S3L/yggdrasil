redis.replicate_commands();
local times = redis.call('time');
local time = string.format("%s%s",times[1],string.sub(times[2],0,3));
local oldTime = redis.call('get',"{key}");
if redis.call('exists',"{key}") > 0 and oldTime > time then time = string.format("%s%s",times[1],string.sub(times[2],0,3)); end 
redis.call('set',"{key}",time);
return time;
