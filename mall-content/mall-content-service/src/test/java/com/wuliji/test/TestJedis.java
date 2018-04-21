package com.wuliji.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedis() {
		//创建一个连接Jedis对象
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		//直接使用jedis操作
		jedis.set("test123", "wuliji");
		System.out.println(jedis.get("test123"));
		jedis.close();
	}
	
	public void testJedisPool() {
		//创建连接池对象，两个整数host，port
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
		//从连接池获得一个连接
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		jedis.set("test123", "wuliji");
		System.out.println(jedis.get("test123"));
		//使用完毕后关闭连接
		jedis.close();
		jedisPool.close();
		
	}
	
	@Test
	public void testJedisCluster() {
		//创建一个jediscluster对象，有一个参数nodes是一个set类型，set中包含若干个HostAndPort对象
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		//直接使用cluster对象关闭
		cluster.set("clusterTest", "wuliji");
		System.out.println(cluster.get("clusterTest"));
		//关闭对象
		cluster.close();
	}
}
